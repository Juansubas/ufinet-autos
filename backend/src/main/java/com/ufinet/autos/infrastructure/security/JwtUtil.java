package com.ufinet.autos.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.HashMap;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    private final ObjectMapper mapper = new ObjectMapper();

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private byte[] hmacSha256(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
        mac.init(secretKeySpec);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username, Long userId) {
        try {
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256");
            header.put("typ", "JWT");

            long now = System.currentTimeMillis();
            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", username);
            payload.put("userId", userId);
            payload.put("iat", now);
            payload.put("exp", now + expirationMs);

            String headerJson = mapper.writeValueAsString(header);
            String payloadJson = mapper.writeValueAsString(payload);

            String headerBase = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadBase = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
            String unsignedToken = headerBase + "." + payloadBase;

            byte[] sig = hmacSha256(secret.getBytes(StandardCharsets.UTF_8), unsignedToken);
            String sigBase = base64UrlEncode(sig);
            return unsignedToken + "." + sigBase;
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new RuntimeException("Invalid token format");
            String headerB = parts[0];
            String payloadB = parts[1];
            String sigB = parts[2];

            String unsigned = headerB + "." + payloadB;
            byte[] expected = hmacSha256(secret.getBytes(StandardCharsets.UTF_8), unsigned);
            String expectedSig = base64UrlEncode(expected);
            if (!expectedSig.equals(sigB)) throw new RuntimeException("Invalid token signature");

            byte[] payloadBytes = Base64.getUrlDecoder().decode(payloadB);
            Map<String, Object> payload = mapper.readValue(payloadBytes, Map.class);

            long exp = ((Number)payload.get("exp")).longValue();
            if (System.currentTimeMillis() > exp) throw new RuntimeException("Token expired");

            return payload;
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException("Token validation error", e);
        }
    }
}

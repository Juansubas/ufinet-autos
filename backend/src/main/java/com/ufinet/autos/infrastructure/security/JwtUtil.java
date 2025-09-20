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

/**
 * Componente de utilidad para la creación y validación de JSON Web Tokens (JWT).
 * Se encarga de toda la lógica criptográfica y de la estructura del token.
 */
@Component
public class JwtUtil {

    // Inyecta la clave secreta desde application.properties (ej: jwt.secret=mi-clave-secreta-muy-larga)
    @Value("${jwt.secret}")
    private String secret;

    // Inyecta el tiempo de expiración en milisegundos desde application.properties (ej: jwt.expiration-ms=86400000 para 24 horas)
    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    // Instancia para convertir objetos Java a JSON y viceversa.
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Codifica un arreglo de bytes a una cadena en formato Base64Url.
     * @param bytes Los datos a codificar.
     * @return La cadena codificada.
     */
    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /**
     * Genera una firma HMAC-SHA256 para un conjunto de datos usando una clave.
     * @param key La clave secreta.
     * @param data Los datos a firmar (header + "." + payload).
     * @return La firma como un arreglo de bytes.
     * @throws Exception Si el algoritmo no está disponible.
     */
    private byte[] hmacSha256(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "HmacSHA256");
        mac.init(secretKeySpec);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un nuevo token JWT para un usuario específico.
     * @param username El nombre de usuario que se incluirá como "subject" del token.
     * @param userId El ID del usuario.
     * @return El token JWT como una cadena de texto.
     */
    public String generateToken(String username, Long userId) {
        try {
            // 1. Crear el Header del JWT
            Map<String, Object> header = new HashMap<>();
            header.put("alg", "HS256"); // Algoritmo de firma
            header.put("typ", "JWT");   // Tipo de token

            // 2. Crear el Payload (claims) del JWT
            long now = System.currentTimeMillis();
            Map<String, Object> payload = new HashMap<>();
            payload.put("sub", username);             // Subject (sujeto del token)
            payload.put("userId", userId);            // Claim personalizado con el ID del usuario
            payload.put("iat", now);                  // Issued At (fecha de emisión)
            payload.put("exp", now + expirationMs);   // Expiration Time (fecha de expiración)

            // Convertir header y payload a JSON
            String headerJson = mapper.writeValueAsString(header);
            String payloadJson = mapper.writeValueAsString(payload);

            // 3. Codificar en Base64Url
            String headerBase = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadBase = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
            String unsignedToken = headerBase + "." + payloadBase;

            // 4. Crear la Firma
            byte[] sig = hmacSha256(secret.getBytes(StandardCharsets.UTF_8), unsignedToken);
            String sigBase = base64UrlEncode(sig);

            // 5. Ensamblar el token final
            return unsignedToken + "." + sigBase;
        } catch (Exception e) {
            // Si algo falla, se lanza una excepción genérica.
            throw new RuntimeException("Error al generar el token", e);
        }
    }

    /**
     * Valida un token JWT. Verifica la firma y la fecha de expiración.
     * @param token El token JWT a validar.
     * @return Un mapa con los claims del payload si el token es válido.
     * @throws RuntimeException si el token es inválido por cualquier motivo.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> validateToken(String token) {
        try {
            // 1. Separar el token en sus tres partes
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new RuntimeException("Formato de token inválido");

            String headerB = parts[0];
            String payloadB = parts[1];
            String sigB = parts[2];

            // 2. Verificar la Firma
            String unsigned = headerB + "." + payloadB;
            byte[] expected = hmacSha256(secret.getBytes(StandardCharsets.UTF_8), unsigned);
            String expectedSig = base64UrlEncode(expected);

            // Compara la firma recibida con la que acabamos de calcular. Si no son iguales, el token es falso o ha sido alterado.
            if (!expectedSig.equals(sigB)) throw new RuntimeException("Firma de token inválida");

            // 3. Decodificar el Payload y verificar la expiración
            byte[] payloadBytes = Base64.getUrlDecoder().decode(payloadB);
            Map<String, Object> payload = mapper.readValue(payloadBytes, Map.class);

            long exp = ((Number)payload.get("exp")).longValue();
            if (System.currentTimeMillis() > exp) throw new RuntimeException("El token ha expirado");

            // Si todo está bien, devuelve el payload
            return payload;
        } catch (RuntimeException re) {
            throw re; // Relanza excepciones específicas de validación
        } catch (Exception e) {
            // Captura cualquier otro error durante el proceso.
            throw new RuntimeException("Error en la validación del token", e);
        }
    }
}
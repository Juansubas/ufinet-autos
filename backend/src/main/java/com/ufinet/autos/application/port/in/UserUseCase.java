package com.ufinet.autos.application.port.in;

import com.ufinet.autos.domain.model.User;
import java.util.Optional;

public interface UserUseCase {
    User register(User user);
    Optional<User> findByUsername(String username);
}

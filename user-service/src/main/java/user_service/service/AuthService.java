package user_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import user_service.model.Role;
import user_service.model.User;
import user_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> register(String name, String email, String password, Role role) {
        String cleanEmail = email.toLowerCase().trim();
        if (userRepository.existsByEmail(cleanEmail)) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .name(name)
                .email(cleanEmail)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User registered successfully");

        return response;

    }

}

package com.lavanderia_colonia.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderia_colonia.api.enums.UserRole;
import com.lavanderia_colonia.api.model.User;
import com.lavanderia_colonia.api.repository.UserRepository;

@RestController
@RequestMapping("/api/v1/setup")
public class SetupController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${security.jwt.token.secret-key:secret}")
    private String jwtSecret;

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody CreateAdminRequest request) {
        try {
            if (!jwtSecret.equals(request.masterPassword())) {
                return ResponseEntity.status(403).body("Senha master inválida");
            }
            if (userRepository.findByName(request.name()).isPresent()) {
                return ResponseEntity.badRequest().body("Usuário já existente");
            }

            User user = new User();
            user.setName(request.name().trim());

            String encodedPassword = passwordEncoder.encode(request.password());
            user.setPassword(encodedPassword);

            user.setRole(UserRole.ADMIN);

            User savedUser = userRepository.save(user);

            return ResponseEntity.ok("Admin criado com sucesso: " + savedUser.getName());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    public record CreateAdminRequest(String name, String password, String masterPassword) {
    }
}
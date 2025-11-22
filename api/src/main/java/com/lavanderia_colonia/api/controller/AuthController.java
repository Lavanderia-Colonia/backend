package com.lavanderia_colonia.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderia_colonia.api.dto.JwtDTO;
import com.lavanderia_colonia.api.dto.SignInDTO;
import com.lavanderia_colonia.api.model.User;
import com.lavanderia_colonia.api.service.TokenProvider;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenProvider tokenService;

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInDTO data) {

        try {
            var namePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

            Authentication authUser = authenticationManager.authenticate(namePassword);

            User user = (User) authUser.getPrincipal();

            var accessToken = tokenService.generateAccessToken(user);

            return ResponseEntity.ok(new JwtDTO(accessToken));

        } catch (AuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
            return ResponseEntity.status(401).body(new ErrorResponse("Credenciais inválidas"));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(500).body(new ErrorResponse("Error: " + e.getMessage()));
        }
    }

    record ErrorResponse(String error) {
    }
}
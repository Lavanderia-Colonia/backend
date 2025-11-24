package com.lavanderia_colonia.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lavanderia_colonia.api.model.User;
import com.lavanderia_colonia.api.service.UserService;

@RestController
@RequestMapping("/api/v1/admin")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<User> getUserMe(Principal principal) {
        return ResponseEntity.ok(userService.findByName(principal.getName()));
    }

    @PutMapping
    public ResponseEntity<User> changeName(Principal principal, @RequestBody ChangeNameRequest request) {
        return ResponseEntity.ok(userService.changeName(principal.getName(), request.newName));
    }

    @PutMapping("/password")
    public ResponseEntity<User> changePassword(Principal principal, @RequestBody ChangePasswordRequest request) {

        if (request.currentPassword() == null || request.newPassword() == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity
                .ok(userService.changePassword(principal.getName(), request.currentPassword(), request.newPassword()));
    }

    public record ChangePasswordRequest(String currentPassword, String newPassword) {
    }

    public record ChangeNameRequest(String newName) {
    }
}
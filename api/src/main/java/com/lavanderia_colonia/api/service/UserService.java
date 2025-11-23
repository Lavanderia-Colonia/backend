package com.lavanderia_colonia.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lavanderia_colonia.api.model.User;
import com.lavanderia_colonia.api.repository.UserRepository;
import com.lavanderia_colonia.api.valueobject.Password;

import io.micrometer.common.lang.NonNull;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByName(String name) {
        return userRepository.findByName(name).get();
    }

    @Transactional
    public User changePassword(@NonNull String name, String password) {
        User user = findByName(name);

        Password passwordValid = new Password(password);

        String encryptedPassword = passwordEncoder.encode(passwordValid.getValue());

        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    @Transactional
    public User changeName(@NonNull String name, String newName) {
        User user = findByName(name);
        user.setName(newName);
        return userRepository.save(user);
    }

}

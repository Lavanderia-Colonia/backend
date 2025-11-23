package com.lavanderia_colonia.api.valueobject;

public class Password {
    private final String value;

    public Password(String value) {
        validate(value);

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    private void validate(String password) {

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Senha inválida");
        }

        if (password.contains(" ")) {
            throw new IllegalArgumentException("Senha inválida");
        }
    }
}

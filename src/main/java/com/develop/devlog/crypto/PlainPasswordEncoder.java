package com.develop.devlog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class PlainPasswordEncoder implements PasswordEncoder {
    @Override
    public String encrypt(String password) {
        return password;
    }

    @Override
    public boolean matches(String password, String encryptPassword) {
        return password.equals(encryptPassword);
    }
}

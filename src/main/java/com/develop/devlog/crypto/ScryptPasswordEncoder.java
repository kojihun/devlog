package com.develop.devlog.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("default")
@Component
public class ScryptPasswordEncoder implements PasswordEncoder{
    private static final SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(16, 8, 1, 32, 64);

    public String encrypt(String password) {
        return encoder.encode(password);
    }

    public boolean matches(String password, String encryptedPassword) {
        return encoder.matches(password, encryptedPassword);
    }
}

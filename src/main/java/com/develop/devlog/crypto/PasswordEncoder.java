package com.develop.devlog.crypto;

public interface PasswordEncoder {

    String encrypt(String password);

    boolean matches(String password, String encryptPassword);
}

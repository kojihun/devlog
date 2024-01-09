package com.develop.devlog.exception;

/**
 * status -> 401
 */
public class Unauthorized extends DevlogException{

    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}

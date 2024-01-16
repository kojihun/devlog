package com.develop.devlog.exception;

public class AlreadyExistsEmailException extends DevlogException{

    private static final String MESSAGE = "이미 가입된 이메일 입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}

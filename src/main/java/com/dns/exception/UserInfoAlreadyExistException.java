package com.dns.exception;

public class UserInfoAlreadyExistException extends RuntimeException {
    public UserInfoAlreadyExistException(String message) {
        super(message);
    }
}

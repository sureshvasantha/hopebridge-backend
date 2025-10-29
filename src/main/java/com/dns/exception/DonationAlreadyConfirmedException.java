package com.dns.exception;

public class DonationAlreadyConfirmedException extends RuntimeException {
    public DonationAlreadyConfirmedException(String message) {
        super(message);
    }
}

package com.dns.exception;

public class InactiveCampaignAccessException extends RuntimeException {
    public InactiveCampaignAccessException(String message) {
        super(message);
    }
}

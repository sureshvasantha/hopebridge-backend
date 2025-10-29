package com.dns.service.impl;

import org.springframework.stereotype.Service;

@Service
public class CurrencyConversionService {

    // For demonstration — replace this with a live rate fetch from Stripe or an API
    public double convertToInr(String currency, double amount) {
        if ("INR".equalsIgnoreCase(currency)) {
            return amount;
        }

        // Example: static rates (you can store these in a config or DB)
        switch (currency.toUpperCase()) {
            case "USD":
                return amount * 91.87; // ₹91.87 per USD (example from Stripe)
            case "EUR":
                return amount * 100.00;
            default:
                throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
    }
}

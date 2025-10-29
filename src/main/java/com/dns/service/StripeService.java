package com.dns.service;

import com.dns.dto.StripeCheckoutRequest;
import com.dns.dto.StripeResponse;

public interface StripeService {

    StripeResponse createCheckoutSession(StripeCheckoutRequest request);

}

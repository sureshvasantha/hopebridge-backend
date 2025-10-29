package com.dns.service.impl;

import com.dns.dto.StripeCheckoutRequest;
import com.dns.dto.StripeResponse;
import com.dns.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeServiceImpl implements StripeService {

	@Value("${stripe.secret.key}")
	private String secretKey;
	@Value("${frontend.success.url}")
	private String successUrl;

	@Value("${frontend.cancel.url}")
	private String cancelUrl;

	public StripeResponse createCheckoutSession(StripeCheckoutRequest request) {
		Stripe.apiKey = secretKey;
		try {
			if (request.getAmount() < 50) {
				return StripeResponse.builder()
						.status("FAILED")
						.message("Minimum donation amount is ₹50 to process via Stripe.")
						.build();
			}

			SessionCreateParams.LineItem.PriceData.ProductData productData = SessionCreateParams.LineItem.PriceData.ProductData
					.builder()
					.setName("Donation for Campaign ID: " + request.getCampaignId())
					.build();

			SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData
					.builder()
					.setCurrency(request.getCurrency() != null ? request.getCurrency() : "INR")
					.setUnitAmount((long) (request.getAmount() * 100)) // in paise/cents
					.setProductData(productData)
					.build();

			SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
					.setQuantity(1L)
					.setPriceData(priceData)
					.build();

			SessionCreateParams params = SessionCreateParams.builder()
					.setMode(SessionCreateParams.Mode.PAYMENT)
					.setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
					.setCancelUrl(cancelUrl)
					.addLineItem(lineItem)
					.build();

			Session session = Session.create(params);

			return StripeResponse.builder()
					.status("SUCCESS")
					.message("Stripe Checkout Session Created")
					.sessionId(session.getId())
					.sessionUrl(session.getUrl())
					.build();

		} catch (StripeException e) {
			return StripeResponse.builder()
					.status("FAILED")
					.message(e.getMessage())
					.build();
		}
	}
}

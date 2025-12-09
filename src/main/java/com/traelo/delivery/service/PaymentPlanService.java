package com.traelo.delivery.service;

import com.traelo.delivery.model.dto.ActivateSubscriptionDTO;
import com.traelo.delivery.model.dto.CreatePaymentPlanDTO;
import com.traelo.delivery.model.dto.PaymentPlanDTO;
import com.traelo.delivery.model.dto.UpdatePaymentPlanDTO;

public interface PaymentPlanService {
	// Create payment plan (start trial)
	PaymentPlanDTO createPaymentPlan(CreatePaymentPlanDTO request);

	// Get plan by businessId
	PaymentPlanDTO getPaymentPlanByBusinessId(Long businessId);

	// Manually activate subscription (after payment verification)
	PaymentPlanDTO activatePaymentPlan(Long id, ActivateSubscriptionDTO request);

	// Cancel subscription
	PaymentPlanDTO cancelPaymentPlan(Long id);

	// Update plan information
	PaymentPlanDTO updatePaymentPlan(Long id, UpdatePaymentPlanDTO request);

	// Check if you have an active subscription
	boolean hasActiveSubscription(Long businessId);

	// Get Mercado Pago URL (FIXED)
	String getMercadoPagoUrl();

	// Mark payment as pending (when user says they have paid)
	PaymentPlanDTO markPaymentAsPending(Long id, String paymentMethod);

	// Check and update expired trials
	void checkAndUpdateExpiredTrials();
}

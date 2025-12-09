package com.traelo.delivery.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentPlanDTO {
	private Long id;
	private Long businessId;
	private Long userId;
	private String planType;
	private String status;
	private String externalReference;
	private Double amountMxn;
	private Integer trialDays;
	private LocalDateTime trialStart;
	private LocalDateTime trialEnd;
	private LocalDateTime nextBillingDate;
	private LocalDateTime lastPaymentDate;
	private String paymentMethod;
	private LocalDateTime createdAt;

	private Boolean isActive;
	private Boolean isTrial;
	private Integer daysRemaining;
	private Boolean hasActiveSubscription;
	private Boolean canActivate;
	private String mercadoPagoUrl; // Fixed URL for everyone
}

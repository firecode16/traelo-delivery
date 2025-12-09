package com.traelo.delivery.enums;

public enum PaymentStatus {
	TRIAL, 		// Active trial period (14 days)
	ACTIVE, 	// Active and paid subscription
	PENDING, 	// Waiting for initial payment
	SUSPENDED, 	// Payment failed/suspended
	CANCELLED, 	// Canceled by user
	EXPIRED 	// Trial expired without payment
}

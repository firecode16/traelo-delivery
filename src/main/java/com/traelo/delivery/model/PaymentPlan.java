package com.traelo.delivery.model;

import java.time.Duration;
import java.time.LocalDateTime;

import com.traelo.delivery.enums.PaymentStatus;
import com.traelo.delivery.enums.PlanType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payment_plans")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "business_id", nullable = false)
	private Long businessId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "plan_type", nullable = false)
	private PlanType planType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	@Column(name = "external_reference", unique = true)
	private String externalReference;

	@Column(name = "amount_mxn", nullable = false)
	private Double amountMxn;

	@Column(name = "trial_days", nullable = false)
	private Integer trialDays;

	@Column(name = "trial_start")
	private LocalDateTime trialStart;

	@Column(name = "trial_end")
	private LocalDateTime trialEnd;

	@Column(name = "next_billing_date")
	private LocalDateTime nextBillingDate;

	@Column(name = "last_payment_date")
	private LocalDateTime lastPaymentDate;

	@Column(name = "payment_method")
	private String paymentMethod;

	@Column(name = "payment_proof_url")
	private String paymentProofUrl; // Receipt URL (WhatsApp)

	@Column(name = "notes")
	private String notes; // Internal notes (e.g., "Paid at OXXO")

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	// Helpers
	public boolean isActive() {
		return status == PaymentStatus.ACTIVE || status == PaymentStatus.TRIAL;
	}

	public boolean isTrial() {
		return status == PaymentStatus.TRIAL;
	}

	public Integer getDaysRemaining() {
		if (trialEnd == null || status != PaymentStatus.TRIAL) {
			return 0;
		}
		long days = Duration.between(LocalDateTime.now(), trialEnd).toDays();
		return (int) Math.max(0, days);
	}
}
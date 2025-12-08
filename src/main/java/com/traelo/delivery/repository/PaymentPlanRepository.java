package com.traelo.delivery.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.PaymentPlan;

@Repository
public interface PaymentPlanRepository extends JpaRepository<PaymentPlan, Long> {
	Optional<PaymentPlan> findByBusinessId(Long businessId);

	@Query("SELECT p FROM PaymentPlan p WHERE p.businessId = :businessId AND p.status IN ('TRIAL', 'ACTIVE')")
	Optional<PaymentPlan> findActiveByBusinessId(@Param("businessId") Long businessId);

	@Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM PaymentPlan p WHERE p.businessId = :businessId AND p.status IN ('TRIAL', 'ACTIVE', 'PENDING')")
	boolean existsActiveByBusinessId(@Param("businessId") Long businessId);

	Optional<PaymentPlan> findByExternalReference(String externalReference);

	@Query("SELECT p FROM PaymentPlan p WHERE p.status = 'TRIAL' AND p.trialEnd <= :date")
	List<PaymentPlan> findExpiringTrials(@Param("date") LocalDateTime date);

	@Query("SELECT p FROM PaymentPlan p WHERE p.status = 'TRIAL' AND p.trialEnd < CURRENT_TIMESTAMP")
	List<PaymentPlan> findExpiredTrials();
}

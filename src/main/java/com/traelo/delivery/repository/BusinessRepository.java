package com.traelo.delivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
	Optional<Business> findByUserId(Long userId);

	Optional<Business> findByBusinessId(Long businessId);
}

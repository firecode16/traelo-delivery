package com.traelo.delivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Rider;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {
	Optional<Rider> findByUserId(Long userId);
}

package com.traelo.delivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Scheduler;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
	Scheduler findByBusinessId(Long businessId);

	Optional<Scheduler> findBySchedulerId(Long schedulerId);

	void deleteBySchedulerId(Long schedulerId);
}

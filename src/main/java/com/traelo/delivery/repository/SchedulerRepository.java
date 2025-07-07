package com.traelo.delivery.repository;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Scheduler;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {
	List<Scheduler> findByBusiness(Business business);

	List<Scheduler> findByBusinessAndDayOfWeek(Business business, DayOfWeek dayOfWeek);
}

package com.traelo.delivery.service.impl;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Scheduler;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.SchedulerRepository;
import com.traelo.delivery.service.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepo;

	@Autowired
	private BusinessRepository businessRepo;

	@Transactional
	@Override
	public Scheduler createSchedule(Scheduler schedule) {
		Business business = businessRepo.findById(schedule.getBusiness().getId()).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
		schedule.setBusiness(business);
		return schedulerRepo.save(schedule);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Scheduler> getSchedulesByBusinessId(Long businessId) {
		Business business = businessRepo.findById(businessId).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
		return schedulerRepo.findByBusiness(business);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Scheduler> getSchedulesByBusinessIdAndDay(Long businessId, DayOfWeek dayOfWeek) {
		Business business = businessRepo.findById(businessId).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
		return schedulerRepo.findByBusinessAndDayOfWeek(business, dayOfWeek);
	}

	@Transactional
	@Override
	public Scheduler updateSchedule(Long id, Scheduler schedule) {
		Scheduler existing = schedulerRepo.findById(id).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
		existing.setDayOfWeek(schedule.getDayOfWeek());
		existing.setOpenTime(schedule.getOpenTime());
		existing.setCloseTime(schedule.getCloseTime());
		existing.setIsActive(schedule.getIsActive());
		return schedulerRepo.save(existing);
	}

	@Transactional
	@Override
	public void deleteSchedule(Long id) {
		schedulerRepo.deleteById(id);
	}
}

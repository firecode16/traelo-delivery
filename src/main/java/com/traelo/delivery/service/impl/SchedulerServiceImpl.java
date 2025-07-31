package com.traelo.delivery.service.impl;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Scheduler;
import com.traelo.delivery.model.dto.SchedulerDTO;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.SchedulerRepository;
import com.traelo.delivery.service.SchedulerService;

@Service
public class SchedulerServiceImpl implements SchedulerService {

	@Autowired
	private SchedulerRepository schedulerRepository;

	@Autowired
	private BusinessRepository businessRepository;

	@Transactional
	@Override
	public Scheduler createSchedule(Scheduler schedule) {
		Optional<Business> business = businessRepository.findByBusinessId(schedule.getBusinessId());
		if (business.isEmpty()) {
			throw new RuntimeException("Negocio no encontrado");
		}
		return schedulerRepository.save(schedule);
	}

	@Transactional(readOnly = true)
	@Override
	public SchedulerDTO getSchedulesByBusinessId(Long businessId) {
		Optional<Business> business = businessRepository.findByBusinessId(businessId);
		if (business.isEmpty()) {
			throw new RuntimeException("Negocio no encontrado");
		}

		Scheduler scheduler = schedulerRepository.findByBusinessId(businessId);
		if (scheduler == null) {
			return null;
		}
		
		return new SchedulerDTO(scheduler.getSchedulerId(), scheduler.getBusinessId(), scheduler.getIsActive());
	}

	@Transactional(readOnly = true)
	@Override
	public List<Scheduler> getSchedulesByBusinessIdAndDay(Long businessId, DayOfWeek dayOfWeek) {
		return new ArrayList<>();
	}

	@Transactional
	@Override
	public Scheduler updateSchedule(Long schedulerId, Scheduler schedule) {
		Scheduler existing = schedulerRepository.findBySchedulerId(schedulerId).orElseThrow(() -> new RuntimeException("Horario no encontrado"));
		existing.setIsActive(schedule.getIsActive());
		return schedulerRepository.save(existing);
	}

	@Transactional
	@Override
	public void deleteSchedule(Long schedulerId) {
		schedulerRepository.deleteBySchedulerId(schedulerId);
	}
}

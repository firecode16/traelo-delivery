package com.traelo.delivery.service;

import java.time.DayOfWeek;
import java.util.List;

import com.traelo.delivery.model.Scheduler;
import com.traelo.delivery.model.dto.SchedulerDTO;

public interface SchedulerService {
	Scheduler createSchedule(Scheduler schedule);

	SchedulerDTO getSchedulesByBusinessId(Long businessId);

	List<Scheduler> getSchedulesByBusinessIdAndDay(Long businessId, DayOfWeek dayOfWeek);

	Scheduler updateSchedule(Long id, Scheduler schedule);

	void deleteSchedule(Long id);
}

package com.traelo.delivery.service;

import java.time.DayOfWeek;
import java.util.List;

import com.traelo.delivery.model.Scheduler;

public interface SchedulerService {
	Scheduler createSchedule(Scheduler schedule);

	List<Scheduler> getSchedulesByBusinessId(Long businessId);

	List<Scheduler> getSchedulesByBusinessIdAndDay(Long businessId, DayOfWeek dayOfWeek);

	Scheduler updateSchedule(Long id, Scheduler schedule);

	void deleteSchedule(Long id);
}

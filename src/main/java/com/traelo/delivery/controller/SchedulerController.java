package com.traelo.delivery.controller;

import java.time.DayOfWeek;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.traelo.delivery.model.Scheduler;
import com.traelo.delivery.service.SchedulerService;

@RestController
@CrossOrigin("*")
public class SchedulerController {

	@Autowired
	private SchedulerService schedulerService;

	@PostMapping("/schedules/create")
	public ResponseEntity<Scheduler> createSchedule(@RequestBody Scheduler schedule) {
		return ResponseEntity.ok(schedulerService.createSchedule(schedule));
	}

	@GetMapping("/schedules/getSchedulesByBusiness/{businessId}")
	public ResponseEntity<List<Scheduler>> getSchedulesByBusiness(@PathVariable Long businessId) {
		return ResponseEntity.ok(schedulerService.getSchedulesByBusinessId(businessId));
	}

	@GetMapping("/schedules/getSchedulesByDay/{businessId}/day/{dayOfWeek}")
	public ResponseEntity<List<Scheduler>> getSchedulesByDay(@PathVariable Long businessId, @PathVariable DayOfWeek dayOfWeek) {
		return ResponseEntity.ok(schedulerService.getSchedulesByBusinessIdAndDay(businessId, dayOfWeek));
	}

	@PutMapping("/schedules/update/{id}")
	public ResponseEntity<Scheduler> updateSchedule(@PathVariable Long id, @RequestBody Scheduler schedule) {
		return ResponseEntity.ok(schedulerService.updateSchedule(id, schedule));
	}

	@DeleteMapping("/schedules/delete/{id}")
	public ResponseEntity<String> deleteSchedule(@PathVariable Long id) {
		schedulerService.deleteSchedule(id);
		return ResponseEntity.ok("Horario eliminado");
	}
}

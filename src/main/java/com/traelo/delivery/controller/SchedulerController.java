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
	public ResponseEntity<Scheduler> getSchedulesByBusiness(@PathVariable Long businessId) {
		Scheduler scheduler = schedulerService.getSchedulesByBusinessId(businessId);
		if (scheduler == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(scheduler);
	}

	@GetMapping("/schedules/getSchedulesByDay/{businessId}/day/{dayOfWeek}")
	public ResponseEntity<List<Scheduler>> getSchedulesByDay(@PathVariable Long businessId, @PathVariable DayOfWeek dayOfWeek) {
		return ResponseEntity.ok(schedulerService.getSchedulesByBusinessIdAndDay(businessId, dayOfWeek));
	}

	@PutMapping("/schedules/update/{schedulerId}")
	public ResponseEntity<Scheduler> updateSchedule(@PathVariable Long schedulerId, @RequestBody Scheduler schedule) {
		return ResponseEntity.ok(schedulerService.updateSchedule(schedulerId, schedule));
	}

	@DeleteMapping("/schedules/delete/{schedulerId}")
	public ResponseEntity<String> deleteSchedule(@PathVariable Long schedulerId) {
		schedulerService.deleteSchedule(schedulerId);
		return ResponseEntity.ok("Horario eliminado");
	}
}

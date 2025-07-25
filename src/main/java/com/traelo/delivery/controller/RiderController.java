package com.traelo.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traelo.delivery.model.Rider;
import com.traelo.delivery.service.RiderService;

@RestController
@RequestMapping("/api/riders")
public class RiderController {

	@Autowired
	private RiderService riderService;

	@PostMapping("/create")
	public ResponseEntity<Rider> create(@RequestBody Rider rider) {
		return ResponseEntity.ok(riderService.createRider(rider));
	}

	@GetMapping("/getByUserId/{userId}")
	public ResponseEntity<Rider> getByUserId(@PathVariable Long userId) {
		return riderService.getRiderByUserId(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping("/getAll")
	public ResponseEntity<List<Rider>> getAll() {
		return ResponseEntity.ok(riderService.getAllRiders());
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Rider> update(@PathVariable Long id, @RequestBody Rider rider) {
		return ResponseEntity.ok(riderService.updateRider(id, rider));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		riderService.deleteRider(id);
		return ResponseEntity.ok("Repartidor eliminado");
	}
}
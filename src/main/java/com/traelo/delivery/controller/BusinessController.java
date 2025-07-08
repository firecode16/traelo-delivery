package com.traelo.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.service.BusinessService;

@RestController
@CrossOrigin("*")
public class BusinessController {

	@Autowired
	private BusinessService businessService;

	@PostMapping("/business/create")
	public ResponseEntity<?> create(@RequestBody Business business) {
		return ResponseEntity.ok(businessService.createBusiness(business));
	}

	@GetMapping("/business/getByUser/{userId}")
	public ResponseEntity<?> getByUser(@PathVariable Long userId) {
		return businessService.getByUserId(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/business/update/{userId}")
	public ResponseEntity<?> update(@PathVariable Long userId, @RequestBody Business data) {
		return ResponseEntity.ok(businessService.updateBusiness(userId, data));
	}

	@GetMapping("/business/getAll")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(businessService.getAllBusinesses());
	}
}
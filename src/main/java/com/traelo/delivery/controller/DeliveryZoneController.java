package com.traelo.delivery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traelo.delivery.model.DeliveryZone;
import com.traelo.delivery.model.dto.DeliveryZoneDTO;
import com.traelo.delivery.model.dto.DeliveryZoneUpdateRequestDTO;
import com.traelo.delivery.service.DeliveryZoneService;

@RestController
@RequestMapping("/api/zones-coverage")
public class DeliveryZoneController {

	@Autowired
	private DeliveryZoneService deliveryZoneService;

	@PostMapping("/create")
	public ResponseEntity<DeliveryZoneDTO> createDeliveryZone(@RequestBody DeliveryZoneDTO deliveryZoneDTO) {
		try {
			DeliveryZoneDTO savedZone = deliveryZoneService.createDeliveryZone(deliveryZoneDTO);
			return ResponseEntity.ok(savedZone);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/getDeliveryZonesByBusiness/{businessId}")
	public List<DeliveryZone> getDeliveryZonesByBusiness(@PathVariable Long businessId) {
		return deliveryZoneService.getDeliveryZonesByBusinessId(businessId);
	}

	@PutMapping("/{businessId}/update-options")
	public ResponseEntity<?> updateDeliveryZoneOptions(@PathVariable Long businessId, @RequestBody DeliveryZoneUpdateRequestDTO dZoneUpdateRequestDTO) {
		try {
			deliveryZoneService.updateDeliveryZoneOptions(businessId, dZoneUpdateRequestDTO);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Error updating delivery options: " + e.getMessage());
		}
	}

}

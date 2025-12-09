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

import com.traelo.delivery.model.dto.ZoneCommissionDTO;
import com.traelo.delivery.model.dto.ZoneCommissionResponseDTO;
import com.traelo.delivery.model.dto.ZoneCommissionUpdateDTO;
import com.traelo.delivery.service.ZoneCommissionService;

@RestController
@RequestMapping("/api/zone-commissions")
public class ZoneCommissionController {
	@Autowired
	private ZoneCommissionService zoneCommissionService;

	@PostMapping("/batch")
	public ResponseEntity<List<ZoneCommissionResponseDTO>> createZoneCommissionsBatch(@RequestBody List<ZoneCommissionDTO> commissions) {
		try {
			List<ZoneCommissionResponseDTO> savedCommissions = zoneCommissionService.createZoneCommissions(commissions);
			return ResponseEntity.ok(savedCommissions);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/create")
	public ResponseEntity<ZoneCommissionResponseDTO> createZoneCommission(@RequestBody ZoneCommissionDTO zoneCommissionDTO) {
		try {
			ZoneCommissionResponseDTO savedCommission = zoneCommissionService.createZoneCommission(zoneCommissionDTO);
			return ResponseEntity.ok(savedCommission);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/getZoneCommissionByBusiness/{businessId}")
	public List<ZoneCommissionResponseDTO> getCommissionsByBusiness(@PathVariable Long businessId) {
		return zoneCommissionService.getZoneCommissionsByBusinessId(businessId);
	}

	@PutMapping("/update-options")
	public ResponseEntity<?> updateZoneCommissionOptions(@RequestBody List<ZoneCommissionUpdateDTO> zCommissionUpdateDTO) {
		try {
			zoneCommissionService.updateZoneCommissionOptions(zCommissionUpdateDTO);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("❌ Error updating zone commission options: " + e.getMessage());
		}
	}

}

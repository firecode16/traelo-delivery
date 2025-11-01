package com.traelo.delivery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traelo.delivery.model.Sector;
import com.traelo.delivery.model.dto.SectorDTO;
import com.traelo.delivery.service.SectorService;

@RestController
@RequestMapping("/api/sectors")
public class SectorController {
	@Autowired
	private SectorService sectorService;

	@PostMapping("/create")
	public ResponseEntity<Sector> createSector(@RequestBody SectorDTO sectorDTO) {
		try {
			Sector savedSector = sectorService.createSector(sectorDTO);
			return ResponseEntity.ok(savedSector);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("/getSectorBySectorId/{sectorId}")
	public ResponseEntity<SectorDTO> getSectorBySectorId(@PathVariable Long sectorId) {
		SectorDTO sectorDTO = sectorService.getSectorBySectorId(sectorId);

		if (sectorDTO == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(sectorDTO);
	}
}

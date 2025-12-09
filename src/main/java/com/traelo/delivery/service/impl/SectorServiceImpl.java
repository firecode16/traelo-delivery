package com.traelo.delivery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Sector;
import com.traelo.delivery.model.dto.SectorDTO;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.SectorRepository;
import com.traelo.delivery.service.SectorService;

@Service
public class SectorServiceImpl implements SectorService {
	@Autowired
	private SectorRepository sectorRepository;
	@Autowired
	private BusinessRepository businessRepository;

	@Transactional
	@Override
	public Sector createSector(SectorDTO sectorDTO) {
		Sector sector = new Sector();
		sector.setSectorId(sectorDTO.getSectorId());
		sector.setName(sectorDTO.getName());
		sector.setDisplayNameProductTab(sectorDTO.getDisplayNameProductTab());
		sector.setIconName(sectorDTO.getIconName());
		sector.setActive(sectorDTO.isActive());

		return sectorRepository.save(sector);
	}

	@Transactional(readOnly = true)
	@Override
	public SectorDTO getSectorBySectorId(Long sectorId) {
		return sectorRepository.findBySectorId(sectorId).map(sector -> {
			SectorDTO dto = new SectorDTO();
			dto.setSectorId(sector.getSectorId());
			dto.setName(sector.getName());
			dto.setDisplayNameProductTab(sector.getDisplayNameProductTab());
			dto.setIconName(sector.getIconName());
			dto.setActive(sector.isActive());
			return dto;
		}).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public SectorDTO getSectorByBusinessId(Long businessId) {
		Business business = businessRepository.findByBusinessId(businessId).orElseThrow(() -> new RuntimeException("Business not found"));

		if (business == null) {
			return null;
		}
		return convertToDTO(business.getSector());
	}

	private SectorDTO convertToDTO(Sector sector) {
		SectorDTO dto = new SectorDTO();
		dto.setSectorId(sector.getSectorId());
		dto.setName(sector.getName());
		dto.setDisplayNameProductTab(sector.getDisplayNameProductTab());
		dto.setIconName(sector.getIconName());
		dto.setActive(sector.isActive());
		return dto;
	}

}

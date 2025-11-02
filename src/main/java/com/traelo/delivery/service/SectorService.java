package com.traelo.delivery.service;

import com.traelo.delivery.model.Sector;
import com.traelo.delivery.model.dto.SectorDTO;

public interface SectorService {
	Sector createSector(SectorDTO sectorDTO);

	SectorDTO getSectorBySectorId(Long sectorId);

	SectorDTO getSectorByBusinessId(Long businessId);
}

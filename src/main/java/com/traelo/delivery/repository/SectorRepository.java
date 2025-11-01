package com.traelo.delivery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Sector;

@Repository
public interface SectorRepository extends JpaRepository<Sector, Long> {
	Optional<Sector> findBySectorId(Long sectorId);
}

package com.traelo.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Business;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
	Page<Business> findBySector_Name(String sectorName, Pageable pageable);

	// Filter by sector and specific area
	@Query(value = "SELECT DISTINCT b.* FROM businesses b " + "INNER JOIN sectors s ON b.sector_id = s.id "
			+ "INNER JOIN delivery_zones dz ON b.business_id = dz.business_aux_id " + "WHERE s.name = :sectorName "
			+ "AND JSON_SEARCH(dz.zones, 'one', :zoneId, NULL, '$[*].id') IS NOT NULL "
			+ "AND b.is_active = true", countQuery = "SELECT COUNT(DISTINCT b.business_id) FROM businesses b "
					+ "INNER JOIN sectors s ON b.sector_id = s.id "
					+ "INNER JOIN delivery_zones dz ON b.business_id = dz.business_aux_id "
					+ "WHERE s.name = :sectorName "
					+ "AND JSON_SEARCH(dz.zones, 'one', :zoneId, NULL, '$[*].id') IS NOT NULL "
					+ "AND b.is_active = true", nativeQuery = true)
	Page<Business> findBySectorAndZone(@Param("sectorName") String sectorName, @Param("zoneId") String zoneId, Pageable pageable);

	// Filter by sector and multiple areas
	@Query(value = "SELECT DISTINCT b.* FROM businesses b " + "INNER JOIN sectors s ON b.sector_id = s.id "
			+ "INNER JOIN delivery_zones dz ON b.business_id = dz.business_aux_id " + "WHERE s.name = :sectorName "
			+ "AND (JSON_SEARCH(dz.zones, 'one', :zoneId1, NULL, '$[*].id') IS NOT NULL "
			+ "OR JSON_SEARCH(dz.zones, 'one', :zoneId2, NULL, '$[*].id') IS NOT NULL) "
			+ "AND b.is_active = true", countQuery = "SELECT COUNT(DISTINCT b.business_id) FROM businesses b "
					+ "INNER JOIN sectors s ON b.sector_id = s.id "
					+ "INNER JOIN delivery_zones dz ON b.business_id = dz.business_aux_id "
					+ "WHERE s.name = :sectorName "
					+ "AND (JSON_SEARCH(dz.zones, 'one', :zoneId1, NULL, '$[*].id') IS NOT NULL "
					+ "OR JSON_SEARCH(dz.zones, 'one', :zoneId2, NULL, '$[*].id') IS NOT NULL) "
					+ "AND b.is_active = true", nativeQuery = true)
	Page<Business> findBySectorAndZones(@Param("sectorName") String sectorName, @Param("zoneId1") String zoneId1, @Param("zoneId2") String zoneId2, Pageable pageable);

	// Filter for multiple zones using JSON_OVERLAPS (MySQL 8.0+)
	@Query(value = "SELECT DISTINCT b.* FROM businesses b " + "INNER JOIN sectors s ON b.sector_id = s.id "
			+ "INNER JOIN delivery_zones dz ON b.business_id = dz.business_aux_id " + "WHERE s.name = :sectorName "
			+ "AND JSON_OVERLAPS(JSON_EXTRACT(dz.zones, '$[*].id'), JSON_ARRAY(:zoneIds)) "
			+ "AND b.is_active = true", countQuery = "SELECT COUNT(DISTINCT b.*) FROM businesses b "
					+ "INNER JOIN sectors s ON b.sector_id = s.id "
					+ "INNER JOIN delivery_zones dz ON b.business_id = dz.business_aux_id "
					+ "WHERE s.name = :sectorName "
					+ "AND JSON_OVERLAPS(JSON_EXTRACT(dz.zones, '$[*].id'), JSON_ARRAY(:zoneIds)) "
					+ "AND b.is_active = true", nativeQuery = true)
	Page<Business> findBySectorAndMultipleZones(@Param("sectorName") String sectorName, @Param("zoneIds") List<String> zoneIds, Pageable pageable);

	@Query(value = "SELECT DISTINCT b.* FROM businesses b " + "INNER JOIN sectors s ON b.sector_id = s.id "
			+ "INNER JOIN delivery_zones dz ON b.business_id = dz.business_aux_id " + "WHERE s.name = :sectorName "
			+ "AND (" + "   :zoneIds LIKE CONCAT('%\"', JSON_UNQUOTE(JSON_EXTRACT(dz.zones, '$[0].id')), '%') OR "
			+ "   :zoneIds LIKE CONCAT('%\"', JSON_UNQUOTE(JSON_EXTRACT(dz.zones, '$[1].id')), '%') OR "
			+ "   :zoneIds LIKE CONCAT('%\"', JSON_UNQUOTE(JSON_EXTRACT(dz.zones, '$[2].id')), '%')" + ") "
			+ "AND b.is_active = true", countQuery = "SELECT COUNT(DISTINCT b.*) FROM businesses b "
					+ "INNER JOIN sectors s ON b.sector_id = s.id "
					+ "INNER JOIN delivery_zones dz ON b.business_id = dz.business_aux_id "
					+ "WHERE s.name = :sectorName " + "AND ("
					+ "   :zoneIds LIKE CONCAT('%\"', JSON_UNQUOTE(JSON_EXTRACT(dz.zones, '$[0].id')), '%') OR "
					+ "   :zoneIds LIKE CONCAT('%\"', JSON_UNQUOTE(JSON_EXTRACT(dz.zones, '$[1].id')), '%') OR "
					+ "   :zoneIds LIKE CONCAT('%\"', JSON_UNQUOTE(JSON_EXTRACT(dz.zones, '$[2].id')), '%')" + ") "
					+ "AND b.is_active = true", nativeQuery = true)
	Page<Business> findBySectorAndMultipleZonesFallback(@Param("sectorName") String sectorName, @Param("zoneIds") String zoneIds, Pageable pageable);

	Optional<Business> findByUserId(Long userId);

	Optional<Business> findByBusinessId(Long businessId);
}

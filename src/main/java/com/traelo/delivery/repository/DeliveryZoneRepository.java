package com.traelo.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.DeliveryZone;

@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {
	Optional<DeliveryZone> findByDeliveryZoneId(Long deliveryZoneId);

	List<DeliveryZone> findByBusiness(Business business);

	List<DeliveryZone> findByBusinessBusinessId(Long businessId);

	@Query("SELECT dz FROM DeliveryZone dz WHERE dz.isActive = true")
	List<DeliveryZone> findAllActiveDeliveryZones();

	@Query(value = "SELECT dz.* FROM delivery_zones dz "
			+ "WHERE JSON_SEARCH(dz.zones, 'one', :zoneId, NULL, '$[*].id') IS NOT NULL " + "AND dz.is_active = true "
			+ "LIMIT 1", nativeQuery = true)
	DeliveryZone findByZoneId(@Param("zoneId") String zoneId);

	@Query(value = "SELECT dz.* FROM delivery_zones dz " + "WHERE dz.zones LIKE CONCAT('%\"', :zoneId, '\"%') "
			+ "AND dz.is_active = true " + "LIMIT 1", nativeQuery = true)
	DeliveryZone findByZoneIdLike(@Param("zoneId") String zoneId);

	default DeliveryZone findDeliveryZoneByZoneId(String zoneId) {
		try {
			System.out.println("🔍 Buscando DeliveryZone para zoneId: " + zoneId);

			// First try with JSON_SEARCH
			DeliveryZone result = findByZoneId(zoneId);
			if (result != null) {
				System.out.println("✅ Encontrado con JSON_SEARCH");
				return result;
			}

			// If it doesn't work, try using LIKE
			System.out.println("🔄 Intentando con LIKE...");
			result = findByZoneIdLike(zoneId);
			
			if (result != null) {
				System.out.println("✅ Encontrado con LIKE");
				return result;
			}

			System.out.println("❌ No se encontró DeliveryZone para: " + zoneId);
			return null;
		} catch (Exception e) {
			System.err.println("❌ Error en findDeliveryZoneByZoneId: " + e.getMessage());
			return null;
		}
	}

}


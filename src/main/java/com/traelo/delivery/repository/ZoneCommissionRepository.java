package com.traelo.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.ZoneCommission;

@Repository
public interface ZoneCommissionRepository extends JpaRepository<ZoneCommission, Long> {

	List<ZoneCommission> findByBusinessBusinessId(Long businessId);

	List<ZoneCommission> findByBusinessBusinessIdAndDeliveryZoneDeliveryZoneId(Long businessId, Long deliveryZoneId);

	List<ZoneCommission> findByBusinessAuxId(Long businessId);

	@Modifying
	@Query("DELETE FROM ZoneCommission zc WHERE zc.zoneCommissionId = :zoneCommissionId AND zc.businessAuxId = :businessAuxId")
	void deleteByZoneCommissionIdAndBusinessAuxId(@Param("zoneCommissionId") Long zoneCommissionId, @Param("businessAuxId") Long businessAuxId);
}

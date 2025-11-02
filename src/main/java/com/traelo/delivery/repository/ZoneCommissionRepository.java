package com.traelo.delivery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.ZoneCommission;

@Repository
public interface ZoneCommissionRepository extends JpaRepository<ZoneCommission, Long> {

	List<ZoneCommission> findByBusinessBusinessId(Long businessId);

	List<ZoneCommission> findByBusinessBusinessIdAndDeliveryZoneDeliveryZoneId(Long businessId, Long deliveryZoneId);
}
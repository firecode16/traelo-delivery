package com.traelo.delivery.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.DeliveryZone;

@Repository
public interface DeliveryZoneRepository extends JpaRepository<DeliveryZone, Long> {
	Optional<DeliveryZone> findByDeliveryZoneId(Long deliveryZoneId);

	List<DeliveryZone> findByBusiness(Business business);

	List<DeliveryZone> findByBusinessBusinessId(Long businessId);
}

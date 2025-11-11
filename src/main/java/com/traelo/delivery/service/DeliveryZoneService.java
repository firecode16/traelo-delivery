package com.traelo.delivery.service;

import java.util.List;

import com.traelo.delivery.model.DeliveryZone;
import com.traelo.delivery.model.dto.DeliveryZoneDTO;
import com.traelo.delivery.model.dto.DeliveryZoneUpdateRequestDTO;

public interface DeliveryZoneService {
	DeliveryZoneDTO createDeliveryZone(DeliveryZoneDTO deliveryZoneDTO);

	List<DeliveryZone> getDeliveryZonesByBusinessId(Long businessId);

	void updateDeliveryZoneOptions(Long businessId, DeliveryZoneUpdateRequestDTO dZoneUpdateRequestDTO);
}

package com.traelo.delivery.service;

import java.util.List;

import com.traelo.delivery.model.DeliveryZone;
import com.traelo.delivery.model.dto.DeliveryZoneDTO;

public interface DeliveryZoneService {
	DeliveryZoneDTO createDeliveryZone(DeliveryZoneDTO deliveryZoneDTO);

	List<DeliveryZone> getDeliveryZonesByBusinessId(Long businessId);
}

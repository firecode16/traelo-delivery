package com.traelo.delivery.service;

import java.util.List;

import com.traelo.delivery.model.DeliveryZone;

public interface ZoneService {
	double calculateDistance(double lat1, double lng1, double lat2, double lng2);

	List<String> findNearbyZoneIds(List<DeliveryZone> allDeliveryZones, double userLat, double userLng, double maxDistanceKm);
}

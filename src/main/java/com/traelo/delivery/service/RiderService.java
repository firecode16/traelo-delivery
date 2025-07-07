package com.traelo.delivery.service;

import java.util.List;
import java.util.Optional;

import com.traelo.delivery.model.Rider;

public interface RiderService {
	Rider createRider(Rider rider);

	Optional<Rider> getRiderByUserId(Long userId);

	Rider updateRider(Long id, Rider rider);

	void deleteRider(Long id);

	List<Rider> getAllRiders();
}
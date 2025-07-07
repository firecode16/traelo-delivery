package com.traelo.delivery.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Rider;
import com.traelo.delivery.repository.RiderRepository;
import com.traelo.delivery.service.RiderService;

@Service
public class RiderServiceImpl implements RiderService {

	@Autowired
	private RiderRepository riderRepository;

	@Transactional
	@Override
	public Rider createRider(Rider rider) {
		return riderRepository.save(rider);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Rider> getRiderByUserId(Long userId) {
		return riderRepository.findByUserId(userId);
	}

	@Transactional
	@Override
	public Rider updateRider(Long id, Rider updatedRider) {
		Rider rider = riderRepository.findById(id).orElseThrow(() -> new RuntimeException("Rider no encontrado"));

		rider.setPhone(updatedRider.getPhone());
		rider.setVehicleType(updatedRider.getVehicleType());
		rider.setLicensePlate(updatedRider.getLicensePlate());
		rider.setIsActive(updatedRider.getIsActive());

		return riderRepository.save(rider);
	}

	@Transactional
	@Override
	public void deleteRider(Long id) {
		riderRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Rider> getAllRiders() {
		return riderRepository.findAll();
	}
}
package com.traelo.delivery.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.service.BusinessService;

@Service
public class BusinessServiceImpl implements BusinessService {

	@Autowired
	private BusinessRepository businessRepository;

	@Transactional
	@Override
	public Business createBusiness(Business business) {
		if (businessRepository.findByUserId(business.getUserId()).isPresent()) {
			throw new RuntimeException("El usuario ya tiene un negocio registrado.");
		}
		return businessRepository.save(business);
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Business> getByUserId(Long userId) {
		return businessRepository.findByUserId(userId);
	}

	@Transactional
	@Override
	public Business updateBusiness(Long userId, Business data) {
		Business existing = businessRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

		existing.setFullName(data.getFullName());
		existing.setDescription(data.getDescription());
		existing.setAddress(data.getAddress());
		existing.setBackdrop(data.getBackdrop());
		existing.setIsActive(data.getIsActive());
		existing.setUpdatedAt(data.getUpdatedAt());

		return businessRepository.save(existing);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Business> getAllBusinesses() {
		return businessRepository.findAll();
	}
}
package com.traelo.delivery.service;

import java.util.List;
import java.util.Optional;

import com.traelo.delivery.model.Business;

public interface BusinessService {
	Business createBusiness(Business business);

	Optional<Business> getByUserId(Long userId);

	Business updateBusiness(Long id, Business business);

	List<Business> getAllBusinesses();
}
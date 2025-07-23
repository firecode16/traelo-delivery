package com.traelo.delivery.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Business;

public interface BusinessService {
	Business createBusiness(Business business);

	Optional<Business> getByUserId(Long userId);

	Business updateBusinessByUserId(Long id, Business business);

	int updateLogoBusinessById(Long businessId, MultipartFile logo) throws IOException;

	byte[] getBusinessLogo(Long businessId);

	List<Business> getAllBusinesses();
}
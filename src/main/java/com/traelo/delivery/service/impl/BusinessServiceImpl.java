package com.traelo.delivery.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
	public Business updateBusinessByUserId(Long userId, Business data) {
		Business existing = businessRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

		existing.setFullName(data.getFullName());
		existing.setDescription(data.getDescription());
		existing.setAddress(data.getAddress());
		existing.setBackdrop(data.getBackdrop());
		existing.setIsActive(data.getIsActive());
		existing.setUpdatedAt(data.getUpdatedAt());

		// Decoder logoBase64 if exist
		if (data.getLogoBase64() != null && !data.getLogoBase64().isEmpty()) {
			try {
				byte[] logoBytes = Base64.getDecoder().decode(data.getLogoBase64());
				existing.setBackdrop(logoBytes);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Error al decodificar la imagen.");
			}
		}

		return businessRepository.save(existing);
	}

	@Transactional
	@Override
	public int updateLogoBusinessById(Long businessId, MultipartFile logoFile) throws IOException {
		int response = 0;

		try {
			byte[] imageBytes = logoFile.getBytes();

			Optional<Business> optBusiness = businessRepository.findByBusinessId(businessId);
			if (optBusiness.isEmpty()) {
				throw new RuntimeException("Negocio no encontrado");
			}

			Business business = optBusiness.get();
			business.setBackdrop(imageBytes);
			business.setUpdatedAt(LocalDateTime.now().toString());
			businessRepository.save(business);
			response = 1;
		} catch (Exception e) {
			response = -1;
			throw new RuntimeCryptoException(e.getMessage());
		}
		return response;
	}

	@Transactional(readOnly = true)
	@Override
	public byte[] getBusinessLogo(Long businessId) {
		Optional<Business> optBusiness = businessRepository.findByBusinessId(businessId);

		if (optBusiness.isEmpty()) {
			throw new RuntimeException("Negocio no encontrado");
		}

		byte[] logoBytes = optBusiness.get().getBackdrop();

		if (logoBytes == null || logoBytes.length == 0) {
			throw new RuntimeException("Este negocio, no cuenta con un logo.");
		}

		return logoBytes;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Business> getAllBusinesses() {
		return businessRepository.findAll();
	}
}
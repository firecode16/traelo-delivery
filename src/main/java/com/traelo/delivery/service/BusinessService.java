package com.traelo.delivery.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.dto.BusinessDTO;
import com.traelo.delivery.model.dto.BusinessDashboardDTO;
import com.traelo.delivery.model.dto.BusinessRequestDTO;
import com.traelo.delivery.model.dto.BusinessUpdateDTO;
import com.traelo.delivery.model.dto.PaymentMethodDTO;
import com.traelo.delivery.model.dto.ZoneInfoDTO;
import com.traelo.delivery.response.PagedResponse;

public interface BusinessService {
	Business createBusiness(BusinessRequestDTO businessRequestDTO);

	BusinessDTO getByUserId(Long userId);

	BusinessUpdateDTO updateBusinessByUserId(Long id, BusinessUpdateDTO businessUpdateDTO);

	int updateLogoBusinessById(Long businessId, MultipartFile logo) throws IOException;

	byte[] getBusinessLogo(Long businessId);

	PagedResponse<BusinessDTO> getAllBusinesses(String sectorName, Double lat, Double lng, String zoneId, Pageable pageable);

	BusinessDashboardDTO getBusinessDashboard(Long businessId);

	void updatePaymentByBusinessId(PaymentMethodDTO paymentMethodDTO);

	List<ZoneInfoDTO> getNearbyZoneIds(Double lat, Double lng, Double maxDistanceKm);
}

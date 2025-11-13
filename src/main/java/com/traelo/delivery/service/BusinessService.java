package com.traelo.delivery.service;

import java.io.IOException;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.dto.BusinessDTO;
import com.traelo.delivery.model.dto.BusinessDashboardDTO;
import com.traelo.delivery.model.dto.BusinessRequestDTO;
import com.traelo.delivery.model.dto.PaymentMethodDTO;
import com.traelo.delivery.response.PagedResponse;

public interface BusinessService {
	Business createBusiness(BusinessRequestDTO businessRequestDTO);

	BusinessDTO getByUserId(Long userId);

	Business updateBusinessByUserId(Long id, Business business);

	int updateLogoBusinessById(Long businessId, MultipartFile logo) throws IOException;

	byte[] getBusinessLogo(Long businessId);

	PagedResponse<BusinessDTO> getAllBusinesses(Pageable pageable);

	BusinessDashboardDTO getBusinessDashboard(Long businessId);

	void updatePaymentByBusinessId(PaymentMethodDTO paymentMethodDTO);
}
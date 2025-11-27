package com.traelo.delivery.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bouncycastle.crypto.RuntimeCryptoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.DeliveryZone;
import com.traelo.delivery.model.ProductVariant;
import com.traelo.delivery.model.Scheduler;
import com.traelo.delivery.model.Sector;
import com.traelo.delivery.model.ZoneCommission;
import com.traelo.delivery.model.dto.BusinessDTO;
import com.traelo.delivery.model.dto.BusinessDashboardDTO;
import com.traelo.delivery.model.dto.BusinessRequestDTO;
import com.traelo.delivery.model.dto.BusinessUpdateDTO;
import com.traelo.delivery.model.dto.DeliveryZoneDTO;
import com.traelo.delivery.model.dto.PaymentMethodDTO;
import com.traelo.delivery.model.dto.ProductDTO;
import com.traelo.delivery.model.dto.ProductVariantDTO;
import com.traelo.delivery.model.dto.SchedulerDTO;
import com.traelo.delivery.model.dto.SectorDTO;
import com.traelo.delivery.model.dto.ZoneCommissionResponseDTO;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.DeliveryZoneRepository;
import com.traelo.delivery.repository.ProductRepository;
import com.traelo.delivery.repository.SchedulerRepository;
import com.traelo.delivery.repository.SectorRepository;
import com.traelo.delivery.repository.ZoneCommissionRepository;
import com.traelo.delivery.response.PagedResponse;
import com.traelo.delivery.response.UserResponse;
import com.traelo.delivery.service.BusinessService;

@Service
public class BusinessServiceImpl implements BusinessService {
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private SchedulerRepository schedulerRepository;
	@Autowired
	private SectorRepository sectorRepository;
	@Autowired
	private DeliveryZoneRepository deliveryZoneRepository;
	@Autowired
	private ZoneCommissionRepository zoneCommissionRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${traelo.security.datauser.url}")
	private String dataUserServiceUrl;

	@Transactional
	@Override
	public Business createBusiness(BusinessRequestDTO businessRequestDTO) {
		if (businessRepository.findByUserId(businessRequestDTO.getUserId()).isPresent()) {
			throw new RuntimeException("El usuario ya tiene un negocio registrado.");
		}
		
		Business business = new Business();
		business.setBusinessId(businessRequestDTO.getBusinessId());
		business.setUserId(businessRequestDTO.getUserId());
		business.setFullName(businessRequestDTO.getFullName());
		business.setDescription(businessRequestDTO.getDescription());
		business.setAddress(businessRequestDTO.getAddress());
		business.setLongitude(businessRequestDTO.getLongitude());
		business.setLatitude(businessRequestDTO.getLatitude());
		business.setBackdrop(businessRequestDTO.getBackdrop());
		business.setIsActive(businessRequestDTO.getIsActive());
		business.setAcceptCash(businessRequestDTO.getAcceptCash());
		business.setAcceptTransfer(businessRequestDTO.getAcceptTransfer());
		business.setBankClabe(businessRequestDTO.getBankClabe());
		business.setBankCard(businessRequestDTO.getBankCard());
		business.setCreatedAt(businessRequestDTO.getCreatedAt());
		business.setUpdatedAt(businessRequestDTO.getUpdatedAt());
		
		if (businessRequestDTO.getSector() != null) {
			Sector sector = sectorRepository.findBySectorId(businessRequestDTO.getSector().getSectorId()).orElseThrow(() -> new RuntimeException("Sector not found"));
			business.setSector(sector);
		}
		return businessRepository.save(business);
	}

	@Transactional(readOnly = true)
	@Override
	public BusinessDTO getByUserId(Long userId) {
		Business business = businessRepository.findByUserId(userId).orElse(null);

		if (business == null) {
			return null;
		}

		BusinessDTO businessDTO = new BusinessDTO();
		businessDTO.setBusinessId(business.getBusinessId());
		businessDTO.setUserId(business.getUserId());
		businessDTO.setFullName(business.getFullName());
		businessDTO.setDescription(business.getDescription());
		businessDTO.setAddress(business.getAddress());
		businessDTO.setIsActive(business.getIsActive());
		businessDTO.setAcceptCash(business.getAcceptCash());
		businessDTO.setAcceptTransfer(business.getAcceptTransfer());
		businessDTO.setBankClabe(business.getBankClabe());
		businessDTO.setBankCard(business.getBankCard());
		businessDTO.setUpdatedAt(business.getUpdatedAt());
		return businessDTO;
	}

	@Transactional
	@Override
	public BusinessUpdateDTO updateBusinessByUserId(Long userId, BusinessUpdateDTO businessUpdateDTO) {
		Business existing = businessRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

		existing.setFullName(businessUpdateDTO.getFullName());
		existing.setDescription(businessUpdateDTO.getDescription());
		existing.setAddress(businessUpdateDTO.getAddress());
		existing.setIsActive(businessUpdateDTO.getIsActive());
		existing.setUpdatedAt(businessUpdateDTO.getUpdatedAt());

		Business business = businessRepository.save(existing);
		BusinessUpdateDTO businessDTO = convertToBusinessUpdateDTO(business);
		return businessDTO;
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
	public PagedResponse<BusinessDTO> getAllBusinesses(Pageable pageable) {
		Page<Business> businessPage = businessRepository.findAll(pageable);

		List<BusinessDTO> content = businessPage.stream().map(b -> {
			List<ProductDTO> products = productRepository.findByBusinessBusinessId(b.getBusinessId()).stream().map(product -> {
				ProductDTO dto = new ProductDTO();
				dto.setProductId(product.getProductId());
				dto.setSectorId(product.getSector().getSectorId());
				dto.setBusinessId(product.getBusiness().getBusinessId());
				dto.setSectorName(product.getSector().getName());
				dto.setName(product.getName());
				dto.setDescription(product.getDescription());
				dto.setPrice(product.getBasePrice());
				dto.setActive(product.isActive());
				dto.setCategory(product.getCategory().getName());
				dto.setGeneralStock(product.getGeneralStock());
				dto.setIngredients(product.getAttributes().get("ingredients"));
				dto.setPreparationTime(product.getPreparationTimeMinutes());
				dto.setBrand(product.getBrand());
				
				List<ProductVariantDTO> variantsDTO = new ArrayList<>();
				List<ProductVariant> variants = product.getVariants();
				variants.forEach(variant -> {
					ProductVariantDTO variantDTO = new ProductVariantDTO();
					variantDTO.setVariantType(variant.getVariantType());
					variantDTO.setVariantValue(variant.getVariantValue());
					variantDTO.setPriceModifier(variant.getPriceModifier());
					
					variantsDTO.add(variantDTO);
				});
				
				dto.setVariants(variantsDTO);
				return dto;
			}).toList();

			Scheduler scheduler = schedulerRepository.findByBusinessId(b.getBusinessId());
			SchedulerDTO schedulerDTO = (scheduler != null) ? new SchedulerDTO(scheduler.getSchedulerId(), scheduler.getBusinessId(), scheduler.getIsActive()) : null;

			UserResponse user = getUserById(b.getUserId());
			return new BusinessDTO(b.getBusinessId(), b.getUserId(), user.getPhone(), b.getFullName(), b.getDescription(), b.getAddress(), b.getIsActive(), b.getAcceptCash(), b.getAcceptTransfer(), b.getBankClabe(), b.getBankCard(), b.getUpdatedAt(), products, schedulerDTO);
		}).toList();

		return new PagedResponse<>(content, businessPage.getNumber(), businessPage.getSize(), businessPage.getTotalPages(), businessPage.getTotalElements(), businessPage.isLast());
	}

	public UserResponse getUserById(Long userId) {
		try {
			String url = dataUserServiceUrl + userId;
			ResponseEntity<UserResponse> response = restTemplate.getForEntity(url, UserResponse.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				return response.getBody();
			} else {
				throw new RuntimeException("No se pudo obtener datos del usuario: " + response.getStatusCode());
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error al obtener datos del usuario desde traelo-security", ex);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public BusinessDashboardDTO getBusinessDashboard(Long businessId) {
	    Business business = businessRepository.findByBusinessId(businessId).orElseThrow(() -> new RuntimeException("Business not found with id: " + businessId));
	    
	    BusinessRequestDTO businessDTO = convertToBusinessDTO(business);
	    SectorDTO sectorDTO = convertToSectorDTO(business.getSector());
	    
	    List<DeliveryZone> deliveryZones = deliveryZoneRepository.findByBusinessBusinessId(businessId);
	    List<DeliveryZoneDTO> deliveryZoneDTOs = deliveryZones.stream().map(this::convertToDeliveryZoneDTO).collect(Collectors.toList());
	    
	    List<ZoneCommission> zoneCommissions = zoneCommissionRepository.findByBusinessBusinessId(businessId);
	    List<ZoneCommissionResponseDTO> zoneCommissionDTO = zoneCommissions.stream().map(this::convertToZoneCommissionResponseDTO).collect(Collectors.toList());
	    
	    return new BusinessDashboardDTO(businessDTO, sectorDTO, deliveryZoneDTOs, zoneCommissionDTO);
	}

	@Transactional
	@Override
	public void updatePaymentByBusinessId(PaymentMethodDTO paymentMethodDTO) {
		if (paymentMethodDTO != null) {
			Business business = businessRepository.findByBusinessId(paymentMethodDTO.getBusinessId()).orElse(null);
			
			try {
				if (business != null) {
					business.setAcceptCash(paymentMethodDTO.getAcceptCash());
					business.setAcceptTransfer(paymentMethodDTO.getAcceptTransfer());
					business.setBankCard(paymentMethodDTO.getBankCard());
					business.setBankClabe(paymentMethodDTO.getBankClabe());
					business.setUpdatedAt(paymentMethodDTO.getUpdatedAt());
				} else {
					System.out.println("ℹ️ No hay Metodo de pago para Actualizar");
				}
			} catch (Exception e) {
				System.err.println("❌ Error updating payment method: " + e.getMessage());
				throw new RuntimeException("❌ Error updating payment method: " + e.getMessage(), e);
			}
		}
	}

	private BusinessRequestDTO convertToBusinessDTO(Business business) {
		BusinessRequestDTO dto = new BusinessRequestDTO();
	    dto.setBusinessId(business.getBusinessId());
	    dto.setFullName(business.getFullName());
	    dto.setDescription(business.getDescription());
	    dto.setAddress(business.getAddress());
	    dto.setLatitude(business.getLatitude());
	    dto.setLongitude(business.getLongitude());
	    dto.setIsActive(business.getIsActive());
	    dto.setAcceptCash(business.getAcceptCash());
	    dto.setAcceptTransfer(business.getAcceptTransfer());
	    dto.setBankClabe(business.getBankClabe());
	    dto.setBankCard(business.getBankCard());
	    dto.setCreatedAt(business.getCreatedAt());
	    dto.setUpdatedAt(business.getUpdatedAt());
	    return dto;
	}

	private SectorDTO convertToSectorDTO(Sector sector) {
		if (sector == null) return null;
	    
	    SectorDTO dto = new SectorDTO();
	    dto.setSectorId(sector.getSectorId());
	    dto.setName(sector.getName());
	    dto.setDisplayNameProductTab(sector.getDisplayNameProductTab());
	    dto.setIconName(sector.getIconName());
	    dto.setActive(sector.isActive());
	    return dto;
	}

	private DeliveryZoneDTO convertToDeliveryZoneDTO(DeliveryZone deliveryZone) {
	    DeliveryZoneDTO dto = new DeliveryZoneDTO();
	    dto.setId(deliveryZone.getId());
	    dto.setBusinessAuxId(deliveryZone.getBusinessAuxId());
	    dto.setDeliveryZoneId(deliveryZone.getDeliveryZoneId());
	    dto.setZoneName(deliveryZone.getZoneName());
	    dto.setPickupEnabled(deliveryZone.getPickupEnabled());
	    dto.setHomeDeliveryEnabled(deliveryZone.getHomeDeliveryEnabled());
	    dto.setDeliveryCentersEnabled(deliveryZone.isDeliveryCentersEnabled());
	    dto.setZones(deliveryZone.getZones());
	    dto.setPoints(deliveryZone.getPoints());
	    dto.setIsActive(deliveryZone.isActive());
	    return dto;
	}

	private ZoneCommissionResponseDTO convertToZoneCommissionResponseDTO(ZoneCommission zoneCommission) {
	    ZoneCommissionResponseDTO dto = new ZoneCommissionResponseDTO();
	    dto.setBusinessAuxId(zoneCommission.getBusinessAuxId());
	    dto.setZoneCommissionId(zoneCommission.getZoneCommissionId());
	    dto.setShippingType(zoneCommission.getShippingType());
	    dto.setSelectedOption(zoneCommission.getSelectedOption());
	    dto.setCommissionAmount(zoneCommission.getCommissionAmount());
	    dto.setAddress(zoneCommission.getAddress());
	    dto.setCoordinates(zoneCommission.getCoordinates());
	    dto.setActive(zoneCommission.isActive());
	    dto.setDeliveryZoneId(zoneCommission.getDeliveryZone().getId());
	    dto.setCreatedAt(zoneCommission.getCreatedAt());
	    dto.setUpdatedAt(zoneCommission.getUpdatedAt());
	    return dto;
	}

	private BusinessUpdateDTO convertToBusinessUpdateDTO(Business business) {
		BusinessUpdateDTO dto = new BusinessUpdateDTO();
		dto.setFullName(business.getFullName());
		dto.setDescription(business.getDescription());
		dto.setAddress(business.getAddress());
		dto.setIsActive(business.getIsActive());
		dto.setUpdatedAt(business.getUpdatedAt());
		return dto;
	}

}

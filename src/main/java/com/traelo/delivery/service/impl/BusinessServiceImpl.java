package com.traelo.delivery.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
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
import com.traelo.delivery.model.Scheduler;
import com.traelo.delivery.model.Sector;
import com.traelo.delivery.model.ZoneCommission;
import com.traelo.delivery.model.dto.BusinessDTO;
import com.traelo.delivery.model.dto.BusinessDashboardDTO;
import com.traelo.delivery.model.dto.BusinessRequestDTO;
import com.traelo.delivery.model.dto.DeliveryZoneDTO;
import com.traelo.delivery.model.dto.MenuDTO;
import com.traelo.delivery.model.dto.SchedulerDTO;
import com.traelo.delivery.model.dto.SectorDTO;
import com.traelo.delivery.model.dto.ZoneCommissionResponseDTO;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.DeliveryZoneRepository;
import com.traelo.delivery.repository.MenuRepository;
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
	private MenuRepository menuRepository;
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
	public Business updateBusinessByUserId(Long userId, Business data) {
		Business existing = businessRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));

		existing.setFullName(data.getFullName());
		existing.setDescription(data.getDescription());
		existing.setAddress(data.getAddress());
		existing.setIsActive(data.getIsActive());
		existing.setAcceptCash(data.getAcceptCash());
		existing.setAcceptTransfer(data.getAcceptTransfer());
		existing.setBankClabe(data.getBankClabe());
		existing.setBankCard(data.getBankCard());
		existing.setUpdatedAt(data.getUpdatedAt());

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
	public PagedResponse<BusinessDTO> getAllBusinesses(Pageable pageable) {
		Page<Business> businessPage = businessRepository.findAll(pageable);

		List<BusinessDTO> content = businessPage.stream().map(b -> {
			List<MenuDTO> menus = menuRepository.findByBusinessId(b.getBusinessId()).stream().map(m -> new MenuDTO(m.getMenuId(), m.getBusinessId(), m.getName(), m.getDescription(), m.getCategory(), m.getPrice(), m.getIsActive(), m.getUpdatedAt())).toList();

			Scheduler scheduler = schedulerRepository.findByBusinessId(b.getBusinessId());
			SchedulerDTO schedulerDTO = (scheduler != null) ? new SchedulerDTO(scheduler.getSchedulerId(), scheduler.getBusinessId(), scheduler.getIsActive()) : null;

			UserResponse user = getUserById(b.getUserId());
			return new BusinessDTO(b.getBusinessId(), b.getUserId(), user.getPhone(), b.getFullName(), b.getDescription(), b.getAddress(), b.getIsActive(), b.getAcceptCash(), b.getAcceptTransfer(), b.getBankClabe(), b.getBankCard(), b.getUpdatedAt(), menus, schedulerDTO);
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
	    dto.setDeliveryZoneId(zoneCommission.getDeliveryZone().getDeliveryZoneId());
	    dto.setCreatedAt(zoneCommission.getCreatedAt());
	    dto.setUpdatedAt(zoneCommission.getUpdatedAt());
	    return dto;
	}

}

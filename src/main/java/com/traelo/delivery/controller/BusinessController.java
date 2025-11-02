package com.traelo.delivery.controller;

import static com.traelo.delivery.util.Util.getImageMimeType;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.dto.BusinessDTO;
import com.traelo.delivery.model.dto.BusinessDashboardDTO;
import com.traelo.delivery.model.dto.BusinessRequestDTO;
import com.traelo.delivery.response.PagedResponse;
import com.traelo.delivery.service.BusinessService;

@RestController
@RequestMapping("/api/business")
public class BusinessController {

	@Autowired
	private BusinessService businessService;

	@PostMapping("/create")
	public ResponseEntity<Business> create(@RequestBody BusinessRequestDTO businessRequestDTO) {
		return ResponseEntity.ok(businessService.createBusiness(businessRequestDTO));
	}

	@GetMapping("/getByUser/{userId}")
	public ResponseEntity<?> getByUser(@PathVariable Long userId) {
		BusinessDTO businessDTO = businessService.getByUserId(userId);

		if (businessDTO == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(businessDTO);
	}

	@PutMapping("/updateBusiness/{userId}")
	public ResponseEntity<?> updateBusinessByUserId(@PathVariable Long userId, @RequestBody Business data) {
		return ResponseEntity.ok(businessService.updateBusinessByUserId(userId, data));
	}

	@PutMapping("/updateLogo/{businessId}")
	public ResponseEntity<?> updateLogoBusinessById(@PathVariable Long businessId, @RequestParam("logo") MultipartFile logoFile) throws IOException {
		if (logoFile.isEmpty()) {
			return ResponseEntity.badRequest().body("No se recibió ningún archivo.");
		}

		// extension is valid
		String originalFilename = logoFile.getOriginalFilename();
		if (originalFilename == null || !originalFilename.matches(".*\\.(jpg|jpeg|png)$")) {
			return ResponseEntity.badRequest().body("Formato de imagen inválido. Usa JPG, JPEG o PNG.");
		}

		return ResponseEntity.ok(businessService.updateLogoBusinessById(businessId, logoFile));
	}

	@GetMapping("/getLogo/{businessId}")
	public ResponseEntity<byte[]> getBusinessLogo(@PathVariable Long businessId) {
		try {
			byte[] logoBytes = businessService.getBusinessLogo(businessId);

			if (logoBytes == null || logoBytes.length == 0) {
				return ResponseEntity.notFound().build();
			}

			// get type MIME
			String mimeType = getImageMimeType(logoBytes);
			if (mimeType == null) {
				return ResponseEntity.badRequest().build();
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(mimeType));
			return new ResponseEntity<>(logoBytes, headers, HttpStatus.OK);
		} catch (Exception ex) {
			ex.getLocalizedMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/getAll")
	public ResponseEntity<PagedResponse<BusinessDTO>> getAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(businessService.getAllBusinesses(pageable));
	}
	
	@GetMapping("/{businessId}/dashboard")
	public ResponseEntity<BusinessDashboardDTO> getBusinessDashboard(@PathVariable Long businessId) {
	    try {
	        BusinessDashboardDTO dashboard = businessService.getBusinessDashboard(businessId);
	        return ResponseEntity.ok(dashboard);
	    } catch (Exception e) {
	    	return ResponseEntity.notFound().build();
	    }
	}

}
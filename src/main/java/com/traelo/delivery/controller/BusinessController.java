package com.traelo.delivery.controller;

import static com.traelo.delivery.util.Util.getImageMimeType;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Business;
import com.traelo.delivery.service.BusinessService;

@RestController
@CrossOrigin("*")
public class BusinessController {

	@Autowired
	private BusinessService businessService;

	@PostMapping("/business/create")
	public ResponseEntity<?> create(@RequestBody Business business) {
		return ResponseEntity.ok(businessService.createBusiness(business));
	}

	@GetMapping("/business/getByUser/{userId}")
	public ResponseEntity<?> getByUser(@PathVariable Long userId) {
		return businessService.getByUserId(userId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/business/updateBusiness/{userId}")
	public ResponseEntity<?> updateBusinessByUserId(@PathVariable Long userId, @RequestBody Business data) {
		return ResponseEntity.ok(businessService.updateBusinessByUserId(userId, data));
	}

	@PutMapping(value = "/business/updateLogo/{businessId}")
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

	@GetMapping("/business/getLogo/{businessId}")
	public ResponseEntity<byte[]> getBusinessLogo(@PathVariable Long businessId) {
		byte[] logoBytes = businessService.getBusinessLogo(businessId);

		// get type MIME
		String mimeType = getImageMimeType(logoBytes);
		if (mimeType == null) {
			return ResponseEntity.badRequest().body(null);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(mimeType));
		return new ResponseEntity<>(logoBytes, headers, HttpStatus.OK);
	}

	@GetMapping("/business/getAll")
	public ResponseEntity<?> getAll() {
		return ResponseEntity.ok(businessService.getAllBusinesses());
	}
}
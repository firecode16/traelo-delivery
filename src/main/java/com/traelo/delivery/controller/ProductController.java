package com.traelo.delivery.controller;

import static com.traelo.delivery.util.Util.getImageMimeType;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traelo.delivery.model.dto.ProductDTO;
import com.traelo.delivery.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@PostMapping(value = "/upsert")
	public ResponseEntity<?> upsertProduct(@RequestParam("productDTO") String productDTOJson, @RequestParam(value = "file", required = false) List<MultipartFile> listFile) {
		try {
			System.out.println("📦 Received productDTO string: " + productDTOJson);
			System.out.println("📁 Received files: " + (listFile != null ? listFile.size() : 0));
			
			ProductDTO productDTO = new ObjectMapper().readValue(productDTOJson, ProductDTO.class);
			
			return ResponseEntity.ok(productService.upsertProduct(productDTO, listFile));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/getProductByProductId/{productId}")
	public ResponseEntity<?> getProductByProductId(@PathVariable Long productId) throws Exception {
		ProductDTO dto = productService.getProductByProductId(productId);

		if (dto == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/getProductsByBusiness/{businessId}")
	public ResponseEntity<?> getProductsByBusiness(@PathVariable Long businessId) {
		try {
			return ResponseEntity.ok(productService.getProductsByBusinessId(businessId));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/delete/{productId}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
		try {
			productService.deleteProduct(productId);
			return ResponseEntity.ok("Producto eliminado");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/getImage/{productId}")
	public ResponseEntity<byte[]> getImageByProduct(@PathVariable Long productId) {
		try {
			byte[] imageBytes = productService.getImageByProduct(productId);

			if (imageBytes == null || imageBytes.length == 0) {
				return ResponseEntity.notFound().build();
			}

			// get type MIME
			String mimeType = getImageMimeType(imageBytes);
			if (mimeType == null) {
				return ResponseEntity.badRequest().build();
			}

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(mimeType));
			return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		} catch (Exception ex) {
			ex.getLocalizedMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}

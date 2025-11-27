package com.traelo.delivery.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.dto.ProductDTO;

public interface ProductService {
	ProductDTO upsertProduct(ProductDTO productDTO, List<MultipartFile> listFile) throws Exception;

	ProductDTO getProductByProductId(Long productId) throws Exception;

	List<ProductDTO> getProductsByBusinessId(Long businessId) throws Exception;

	void deleteProduct(Long productId) throws Exception;

	byte[] getImageByProduct(Long productId);
}

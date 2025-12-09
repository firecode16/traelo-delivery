package com.traelo.delivery.service.impl;

import static com.traelo.delivery.util.Util.generateUniqueID;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.traelo.delivery.model.Bucket;
import com.traelo.delivery.model.BucketImage;
import com.traelo.delivery.model.Business;
import com.traelo.delivery.model.Category;
import com.traelo.delivery.model.Product;
import com.traelo.delivery.model.ProductVariant;
import com.traelo.delivery.model.Sector;
import com.traelo.delivery.model.dto.ProductDTO;
import com.traelo.delivery.model.dto.ProductVariantDTO;
import com.traelo.delivery.repository.BucketImageRepository;
import com.traelo.delivery.repository.BucketRepository;
import com.traelo.delivery.repository.BusinessRepository;
import com.traelo.delivery.repository.CategoryRepository;
import com.traelo.delivery.repository.ProductRepository;
import com.traelo.delivery.repository.ProductVariantRepository;
import com.traelo.delivery.repository.SectorRepository;
import com.traelo.delivery.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductVariantRepository productVariantRepository;
	@Autowired
	private BucketRepository bucketRepository;
	@Autowired
	private BucketImageRepository bucketImageRepository;
	@Autowired
	private BusinessRepository businessRepository;
	@Autowired
	private SectorRepository sectorRepository;

	@Transactional
	@Override
	public ProductDTO upsertProduct(ProductDTO productDTO, List<MultipartFile> listFile) throws Exception {
		// 1. Validate DTO
		validateProductDTO(productDTO);

		// 2. Validate that new products have at least one image
		if (productDTO.getProductId() == null && (listFile == null || listFile.isEmpty())) {
			throw new IllegalArgumentException("Los productos nuevos deben tener al menos una imagen.");
		}
		
		// 3. Save Category
		Category category = saveCategory(productDTO);

		// 4. Save or Update product
		Product product = saveOrUpdateProduct(productDTO, category);

		// 5. Save or Update ProductVariants
		List<ProductVariant> variants = saveOrUpdateProductVariants(productDTO.getVariants(), product);

		// 6. Save Bucket y BucketImage
		if (listFile != null && !listFile.isEmpty()) {
			createBucketForProducts(productDTO, listFile, variants, category, product);
		} else {
			System.out.println("No hay archivos nuevos para procesar, manteniendo imágenes existentes");
		}

		return convertToResponseDTO(product);
	}

	@Transactional(readOnly = true)
	@Override
	public ProductDTO getProductByProductId(Long productId) throws Exception {
		return productRepository.findByProductId(productId).map(this::convertToResponseDTO).orElse(null);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ProductDTO> getProductsByBusinessId(Long businessId) throws Exception {
		return productRepository.findByBusinessBusinessId(businessId).stream().map(this::convertToResponseDTO).toList();
	}

	@Transactional
	@Override
	public void deleteProduct(Long productId) throws Exception {
		Product product = productRepository.findByProductId(productId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));

		final Long businessId = product.getBusiness().getBusinessId();
		final Long sectorId = product.getSector().getSectorId();

		// Delete Buckets relations (and images by cascade)
		deleteExistingBucketsForProduct(productId, businessId, sectorId);

		// Delete ProductVariants
		productVariantRepository.deleteByProductProductId(productId);

		// Delete Product
		productRepository.deleteByProductId(productId);

		System.out.println("Producto eliminado: " + productId);
	}

	private void validateProductDTO(ProductDTO productDTO) {
		if (productDTO.getSectorId() == null) {
			throw new IllegalArgumentException("sectorId es requerido");
		}
		if (productDTO.getBusinessId() == null) {
			throw new IllegalArgumentException("businessId es requerido");
		}
		if (productDTO.getSectorName() == null || productDTO.getSectorName().trim().isEmpty()) {
			throw new IllegalArgumentException("sectorName es requerido");
		}
	}

	private Category saveCategory(ProductDTO productDTO) {
		// Search for existing category by name and sector
		Optional<Category> existingCategory = categoryRepository.findByNameAndSectorId(productDTO.getCategory(), productDTO.getSectorId());

		if (existingCategory.isPresent()) {
			// If the category already exists, we return it
			Category category = existingCategory.get();
			System.out.println("Usando categoría existente: " + category.getName());
			return category;
		} else {
			// Create new category
			Sector sector = sectorRepository.findBySectorId(productDTO.getSectorId()).orElseThrow(() -> new RuntimeException("Sector no encontrado"));

			Category newCategory = new Category();
			newCategory.setName(productDTO.getCategory());
			newCategory.setSector(sector);
			newCategory.setCategoryId(generateUniqueID());

			System.out.println("Creando nueva categoría: " + newCategory.getName());
			return categoryRepository.save(newCategory);
		}
	}

	private Product saveOrUpdateProduct(ProductDTO productDTO, Category category) {
		Business business = businessRepository.findByBusinessId(productDTO.getBusinessId()).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
		Sector sector = sectorRepository.findBySectorId(productDTO.getSectorId()).orElseThrow(() -> new RuntimeException("Sector no encontrado"));

		// Search for existing product
		Product product;
		boolean isNewProduct = false;

		if (productDTO.getProductId() != null) {
			// If it comes with productId, look for an existing product
			product = productRepository.findByProductIdAndBusinessBusinessId(productDTO.getProductId(), productDTO.getBusinessId()).orElse(new Product());

			if (product.getId() == null) {
				// If it wasn't found but came with an ID, use that ID
				product.setProductId(productDTO.getProductId());
				isNewProduct = true;
			}
		} else {
			// Create new product
			product = new Product();
			product.setProductId(generateUniqueID());
			isNewProduct = true;
		}

		if (isNewProduct) {
			product.setBusiness(business);
			product.setSector(sector);
			product.setCreatedAt(new Date());
			product.setUpdatedAt(new Date());
			
			System.out.println("Creando nuevo producto: " + product.getProductId());
		} else {
			product.setUpdatedAt(new Date());
			System.out.println("Actualizando producto existente: " + product.getProductId());
		}

		// Update common fields
		product.setName(productDTO.getName());
		product.setDescription(productDTO.getDescription());
		product.setBasePrice(productDTO.getPrice());
		product.setActive(productDTO.isActive());
		product.setCategory(category);

		// Configure sector-specific fields
		configureSectorSpecificFields(productDTO, product);

		return productRepository.save(product);
	}

	private void configureSectorSpecificFields(ProductDTO productDTO, Product product) {
		switch (productDTO.getSectorName()) {
		case "food" -> {
			product.setGeneralStock(productDTO.getGeneralStock());
			product.setPreparationTimeMinutes(productDTO.getPreparationTime());
			product.setBrand(productDTO.getBrand() != null ? productDTO.getBrand() : "");

			Map<String, String> attributes = new HashMap<>();
			if (productDTO.getIngredients() != null) {
				attributes.put("ingredients", productDTO.getIngredients());
			}
			product.setAttributes(attributes);
		}
		case "fashion" -> {
			product.setGeneralStock(productDTO.getGeneralStock());
			product.setBrand(productDTO.getBrand());
			// For fashion, the attributes could include sizes, colors, etc.
			Map<String, String> attributes = new HashMap<>();
			// Here you can add specific fashion attributes
			product.setAttributes(attributes);
			System.out.println("Sector Moda y Calzado - Configurando campos específicos");
		}
		case "technology" -> {
			product.setGeneralStock(productDTO.getGeneralStock());
			product.setBrand(productDTO.getBrand());
			// For technology, the attributes could include technical specifications
			Map<String, String> attributes = new HashMap<>();
			// Here you can add specific technology attributes
			product.setAttributes(attributes);
			System.out.println("Sector Electrónica y Tecnología - Configurando campos específicos");
		}
		default -> throw new IllegalArgumentException("Sector no soportado: " + productDTO.getSectorName());
		}
	}

	private List<ProductVariant> saveOrUpdateProductVariants(List<ProductVariantDTO> variantsDTO, Product product) {
	    System.out.println("🔄 Procesando variantes para producto: " + product.getProductId());
	    System.out.println("📦 Variantes recibidas en DTO: " + (variantsDTO != null ? variantsDTO.size() : 0));

	    // Delete ALL existing variants first (simple strategy)
	    if (product.getId() != null) {
	        System.out.println("🗑️ Eliminando todas las variantes existentes para producto: " + product.getProductId());
	        productVariantRepository.deleteByProductProductId(product.getProductId());
	    }

	    if (variantsDTO == null || variantsDTO.isEmpty()) {
	        System.out.println("📭 No hay variantes para crear");
	        return Collections.emptyList();
	    }

	    // Filter duplicate variants using a Map
	    Map<String, ProductVariantDTO> uniqueVariantsMap = new HashMap<>();
	    
	    variantsDTO.forEach(variantDTO -> {
	    	if (variantDTO.getVariantType() != null && variantDTO.getVariantValue() != null) {
	    		String key = (variantDTO.getVariantType().trim().toLowerCase() + "|" + variantDTO.getVariantValue().trim().toLowerCase());
	            uniqueVariantsMap.put(key, variantDTO);
	    	}
	    });

	    System.out.println("🎯 Variantes únicas después de filtrado: " + uniqueVariantsMap.size());

	    // Create news unique variants
	    List<ProductVariant> newVariants = new ArrayList<>();
	    
	    uniqueVariantsMap.values().forEach(variantDTO -> {
	    	ProductVariant variant = new ProductVariant();
	        variant.setProductVariantId(generateUniqueID());
	        variant.setVariantType(variantDTO.getVariantType());
	        variant.setVariantValue(variantDTO.getVariantValue());
	        variant.setPriceModifier(variantDTO.getPriceModifier() != null ? variantDTO.getPriceModifier() : BigDecimal.ZERO);
	        variant.setProduct(product);
	        
	        newVariants.add(variant);
	    });

	    System.out.println("💾 Guardando " + newVariants.size() + " variantes");
	    
	    return productVariantRepository.saveAll(newVariants);
	}

	private void createBucketForProducts(ProductDTO productDTO, List<MultipartFile> listFile, List<ProductVariant> variants, Category category, Product product) {
		if (listFile == null || listFile.isEmpty()) {
			System.out.println("No hay archivos para procesar - manteniendo imágenes existentes");
			return;
		}

		try {
			Business business = businessRepository.findByBusinessId(productDTO.getBusinessId()).orElseThrow(() -> new RuntimeException("Negocio no encontrado"));
			Sector sector = sectorRepository.findBySectorId(productDTO.getSectorId()).orElseThrow(() -> new RuntimeException("Sector no encontrado"));

			switch (productDTO.getSectorName()) {
			case "food" -> {
				// If we are updating, delete existing buckets for this product
				if (productDTO.getProductId() != null && listFile != null && !listFile.isEmpty()) {
					deleteExistingBucketsForProduct(productDTO.getProductId(), business.getBusinessId(), sector.getSectorId());
				}

				// Create new buckets with the images
				for (MultipartFile file : listFile) {
					if (!file.isEmpty()) {
						Bucket bucket = new Bucket();
						bucket.setObjectId(UUID.randomUUID().toString());
						bucket.setName(file.getOriginalFilename());
						bucket.setMimeType(file.getContentType());
						bucket.setSize(file.getSize());
						bucket.setBusiness(business);
						bucket.setSector(sector);
						bucket.setCategory(category);
						bucket.setProduct(product);

						Bucket savedBucket = bucketRepository.save(bucket);

						BucketImage bucketImage = new BucketImage();
						bucketImage.setImageData(file.getBytes());
						bucketImage.setBucket(savedBucket);
						bucketImageRepository.save(bucketImage);

						System.out.println("Imagen guardada en BucketImage: " + bucketImage.getId());
					}
				}
			}
			case "fashion" -> {
				System.out.println("Sector Moda y Calzado - Preparado para manejar imágenes por variante");

				// If we are updating, delete existing buckets
				if (productDTO.getProductId() != null) {
					deleteExistingBucketsForProduct(productDTO.getProductId(), business.getBusinessId(), sector.getSectorId());
				}

				// Future implementation: associate images with specific variants
				// For now, we save all images without specific association
				for (MultipartFile file : listFile) {
					if (!file.isEmpty()) {
						Bucket bucket = new Bucket();
						bucket.setObjectId(UUID.randomUUID().toString());
						bucket.setName(file.getOriginalFilename());
						bucket.setMimeType(file.getContentType());
						bucket.setSize(file.getSize());
						bucket.setBusiness(business);
						bucket.setSector(sector);
						bucket.setCategory(category);
						bucket.setProduct(product);

						Bucket savedBucket = bucketRepository.save(bucket);

						BucketImage bucketImage = new BucketImage();
						bucketImage.setImageData(file.getBytes());
						bucketImage.setBucket(savedBucket);
						bucketImageRepository.save(bucketImage);
					}
				}
			}
			case "technology" -> {
				System.out.println("Sector Electrónica y Tecnología - Preparado para manejar imágenes por variante");

				// If we are updating, delete existing buckets
				if (productDTO.getProductId() != null) {
					deleteExistingBucketsForProduct(productDTO.getProductId(), business.getBusinessId(), sector.getSectorId());
				}

				// Future implementation: associate images with specific variants
				// For now, we save all images without specific association
				for (MultipartFile file : listFile) {
					if (!file.isEmpty()) {
						Bucket bucket = new Bucket();
						bucket.setObjectId(UUID.randomUUID().toString());
						bucket.setName(file.getOriginalFilename());
						bucket.setMimeType(file.getContentType());
						bucket.setSize(file.getSize());
						bucket.setBusiness(business);
						bucket.setSector(sector);
						bucket.setCategory(category);
						bucket.setProduct(product);

						Bucket savedBucket = bucketRepository.save(bucket);

						BucketImage bucketImage = new BucketImage();
						bucketImage.setImageData(file.getBytes());
						bucketImage.setBucket(savedBucket);
						bucketImageRepository.save(bucketImage);
					}
				}
			}
			default -> throw new IllegalArgumentException("Sector no soportado: " + productDTO.getSectorName());
			}
		} catch (Exception e) {
			System.err.println("Error al crear buckets para producto: " + e.getMessage());
			throw new RuntimeException("Error al procesar imágenes del producto", e);
		}
	}

	private void deleteExistingBucketsForProduct(Long productId, Long businessId, Long sectorId) {
		try {
			// Search for all existing buckets for this business and sector
			List<Bucket> existingBuckets = bucketRepository.findByProductProductIdAndBusinessBusinessIdAndSectorSectorId(productId, businessId, sectorId);

			if (!existingBuckets.isEmpty()) {
				System.out.println("Eliminando " + existingBuckets.size() + " buckets existentes para producto: " + productId);

				// Only delete the buckets of this specific product
				bucketRepository.deleteAll(existingBuckets);
			} else {
				System.out.println("No se encontraron buckets previos para el producto: " + productId);
			}
		} catch (Exception e) {
			System.err.println("Error al eliminar buckets existentes: " + e.getMessage());
		}
	}

	private ProductDTO convertToResponseDTO(Product product) {
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

		if (product.getAttributes() != null) {
			String ingredients = product.getAttributes().get("ingredients");
			dto.setIngredients(ingredients);
		}

		dto.setPreparationTime(product.getPreparationTimeMinutes());

		List<ProductVariantDTO> variants = new ArrayList<>();
		if (product.getVariants() != null) {
			product.getVariants().forEach(variant -> {
				ProductVariantDTO variantDTO = new ProductVariantDTO(variant.getVariantType(), variant.getVariantValue(), variant.getPriceModifier());
				variants.add(variantDTO);
			});
		}

		dto.setVariants(variants);
		return dto;
	}

	@Transactional(readOnly = true)
	@Override
	public byte[] getImageByProduct(Long productId) {
		try {
			Product product = productRepository.findByProductId(productId).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
			
			// Search for the Bucket ASSOCIATED with that product
			Bucket bucketMetadata = bucketRepository.findByProduct(product).orElse(null);
			
			if (bucketMetadata != null) {
				return bucketMetadata.getBucketImage().getImageData();
			} else {
				throw new RuntimeException("El Bucket no tiene imagen binaria");
			}
		} catch (Exception e) {
			System.err.println("Error obteniendo imagen: " + e.getMessage());
			return null;
		}
	}

}
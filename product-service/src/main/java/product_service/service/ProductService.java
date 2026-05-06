package product_service.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import product_service.dto.ProductDTO.ProductInput;
import product_service.dto.ProductDTO.ProductOutput;
import product_service.exception.NotFoundException;
import product_service.mapper.ProductMapper;
import product_service.model.Product;
import product_service.repository.ProductRepository;
import product_service.restApi.MediaClient;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MediaClient mediaClient;

    public ProductOutput createProduct(ProductInput productData, String userId) {
        Product product = Product.builder()
                .name(productData.name())
                .description(productData.description())
                .price(productData.price())
                .quantity(productData.quantity())
                .userId(userId)
                .build();

        Product createdProduct = this.productRepository.insert(product);
        return ProductMapper.toProductOutputDto(createdProduct, null);
    }

    public ProductOutput getProduct(String productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Whoops! product not found"));

        List<String> productFiles = this.mediaClient.getProductMedia(product.getId());
        return ProductMapper.toProductOutputDto(product, productFiles);
    }

    public ProductOutput updateProduct(String productId, ProductInput productData, String userId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Whoops! product not found"));

        if (!userId.equals(product.getUserId())) {
            throw new AccessDeniedException("Cannot update products of others!");
        }

        product.setName(productData.name());
        product.setDescription(productData.description());
        product.setPrice(productData.price());
        product.setQuantity(productData.quantity());

        this.productRepository.save(product);

        return ProductMapper.toProductOutputDto(product, null);
    }

    public Map<String, String> deleteProduct(String productId, String userId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Whoops! product not found"));

        if (!userId.equals(product.getUserId())) {
            throw new AccessDeniedException("Cannot delete products of others!");
        }

        this.productRepository.delete(product);

        return Map.of(
                "productId", product.getId(),
                "message", "Product deleted successfully!");
    }

}

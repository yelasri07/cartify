package product_service.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import product_service.dto.ProductDTO.ProductInput;
import product_service.dto.ProductDTO.ProductOutput;
import product_service.exception.NotFoundException;
import product_service.mapper.ProductMapper;
import product_service.model.Product;
import product_service.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductOutput createProduct(ProductInput productData, String userId) {
        Product product = Product.builder()
                .name(productData.name())
                .description(productData.description())
                .price(productData.price())
                .quantity(productData.quantity())
                .userId(userId)
                .build();

        Product createdProduct = this.productRepository.insert(product);
        return ProductMapper.toProductOutputDto(createdProduct);
    }

    public ProductOutput getProduct(String productId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Whoops! product not found"));
        return ProductMapper.toProductOutputDto(product);
    }

}

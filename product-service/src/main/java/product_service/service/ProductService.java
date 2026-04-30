package product_service.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import product_service.dto.ProductDTO.ProductInput;
import product_service.dto.ProductDTO.ProductOutput;
import product_service.model.Product;
import product_service.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductOutput createProduct(ProductInput productData) {
        Product product = Product.builder()
                .name(productData.name())
                .description(productData.description())
                .price(productData.price())
                .quantity(productData.quantity())
                .userId(null)
                .build();

        Product createdProduct = this.productRepository.insert(product);

        return ProductOutput.builder()
                .id(createdProduct.getId())
                .name(createdProduct.getName())
                .discription(createdProduct.getDescription())
                .price(createdProduct.getPrice())
                .quantity(createdProduct.getQuantity())
                .userId(createdProduct.getUserId())
                .build();
    }

}

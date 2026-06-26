package product_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import product_service.dto.ProductDTO.ProductInput;
import product_service.dto.ProductDTO.ProductOutput;
import product_service.exception.NotFoundException;
import product_service.model.Product;
import product_service.model.ProductStatus;
import product_service.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService unit tests")
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductInput productInput;
    private Product productTest;

    @BeforeEach
    void setup() {
        productInput = new ProductInput("adidas", "good product", 8.5, 20);
        productTest = Product.builder()
                .name(productInput.name())
                .description(productInput.description())
                .price(productInput.price())
                .quantity(productInput.quantity())
                .status(ProductStatus.PENDING)
                .userId("1")
                .id("a5SD45vsdf")
                .build();
    }

    @DisplayName("Create product unit tests")
    @Nested
    class CreateProductTests {

        @Test
        void shouldCreateProductSuccessfully() {
            // Given
            String userId = "1";
            when(productRepository.insert(any(Product.class)))
                    .thenReturn(productTest);

            // When
            ProductOutput product = productService.createProduct(productInput, userId);

            // Then
            assertNotNull(product);
            assertEquals(productInput.name(), product.name());
            verify(productRepository).insert(any(Product.class));
        }
    }

    @DisplayName("Get products unit tests")
    @Nested
    class GetProductsTests {

        @Test
        void shouldThrowNotFoundExceptionWhenProductNotFound() {
            // Given
            String productId = "product-id";
            when(productRepository.findById(productId))
                    .thenReturn(Optional.empty());

            // When & Then
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> productService.getProduct(productId));

            assertNotNull(exception);
            assertEquals("Whoops! product not found", exception.getMessage());
            verify(productRepository).findById(productId);
        }

    }

}

package product_service.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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
import product_service.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService unit tests")
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductInput productInput;

    @BeforeEach
    void setup() {
        productInput = new ProductInput("adidas", "good product", 8.5, 20);
    }

    @DisplayName("Create product unit tests")
    @Nested
    class createProductTests {

        @Test
        void shouldCreateProductSuccessfully() {
            // Given
            String userId = "1";

            // When
            when(null);
            ProductOutput product = productService.createProduct(productInput, userId);

            // Then
            assertNotNull(product);
        }
    }

}

package product_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import product_service.dto.UserDTO;
import product_service.dto.ProductDTO.ProductInput;
import product_service.dto.ProductDTO.ProductOutput;
import product_service.exception.BadRequestException;
import product_service.exception.NotFoundException;
import product_service.model.Product;
import product_service.model.ProductStatus;
import product_service.repository.ProductRepository;
import product_service.restApi.MediaClient;
import product_service.restApi.UserClient;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService unit tests")
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private MediaClient mediaClient;
    @Mock
    private UserClient userClient;

    @InjectMocks
    private ProductService productService;

    private ProductInput productInput;
    private Product productResponse;
    private UserDTO userResponse;

    @BeforeEach
    void setup() {
        productInput = new ProductInput("adidas", "good product", 8.5, 20);
        productResponse = Product.builder()
                .name(productInput.name())
                .description(productInput.description())
                .price(productInput.price())
                .quantity(productInput.quantity())
                .status(ProductStatus.PENDING)
                .userId("1")
                .id("a5SD45vsdf")
                .build();
        userResponse = UserDTO.builder()
                .name("Youssef")
                .avatar("https://profile.com/516")
                .build();
    }

    @DisplayName("Create product unit tests")
    @Nested
    class CreateProductTests {

        @Test
        void shouldCreateProductSuccessfully() {
            // Given
            final String userId = "1";
            when(productRepository.insert(any(Product.class)))
                    .thenReturn(productResponse);

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
        void shouldGetProductByIdSuccessfully() {
            // Arrange
            final String productId = "product-id";
            when(productRepository.findById(productId))
                    .thenReturn(Optional.of(productResponse));
            when(mediaClient.getProductMedia(anyString()))
                    .thenReturn(List.of("media-url1", "media-url2"));

            // Act
            ProductOutput product = productService.getProduct(productId);

            // Assert
            assertNotNull(product);
            assertEquals(productResponse.getName(), product.name());
            verify(productRepository).findById(productId);
            verify(mediaClient).getProductMedia(anyString());
        }

        @Test
        @DisplayName("Should get products successfully")
        void shouldGetProductsSuccessfully() {
            // Arrange
            final int page = 0;
            final int size = 10;
            when(productRepository.findByStatus(any(), any()).getContent())
                    .thenReturn(List.of(productResponse));
            // when(null);
            
            // Act

            // Assert
        }

        @Test
        @DisplayName("Should throw bad request exception when get products with invalid size")
        void shouldThrowBadRequestExceptionWhenGetProductsWithInvalidSize() {
            // Arrange
            final int page = 0;
            final int size = 101;
            final String userId = "user-id";

            // Act & Assert
            BadRequestException exception = assertThrows(BadRequestException.class,
                    () -> productService.getProducts(page, size, userId));

            assertNotNull(exception);
            assertEquals("Max size is: 100", exception.getMessage());
        }

        @Test
        void shouldThrowNotFoundExceptionWhenProductNotFound() {
            // Given
            final String productId = "product-id";
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

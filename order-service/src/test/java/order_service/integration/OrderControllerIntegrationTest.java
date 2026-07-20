package order_service.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import order_service.dto.ProductDTO.ProductOutput;
import order_service.kafka.OrderProducer;
import order_service.model.CartItem;
import order_service.model.OrderDetails;
import order_service.model.OrderStatus;
import order_service.model.ShoppingCart;
import order_service.repository.CartItemRepository;
import order_service.repository.OrderDetailsRepository;
import order_service.repository.OrderItemRepository;
import order_service.repository.ShoppingCartRepository;
import order_service.restApi.ProductClient;
import order_service.service.JwtService;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class OrderControllerIntegrationTest {

    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0");

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @MockitoBean
    private ProductClient productClient;

    @MockitoBean
    private OrderProducer orderProducer;

    @MockitoBean
    private JwtService jwtService;

    private String userId = "client-user-1";
    private String token = "Bearer valid-token";
    private String productId = "prod-1";

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        orderItemRepository.deleteAll();
        orderDetailsRepository.deleteAll();
        cartItemRepository.deleteAll();
        shoppingCartRepository.deleteAll();

        // Mock JWT authentication for client user
        when(jwtService.extractUserId("valid-token")).thenReturn(Map.of("id", userId, "role", "CLIENT"));

        // Mock Product microservice Feign response
        ProductOutput product = ProductOutput.builder()
                .id(productId)
                .name("Smart Phone")
                .price(100.0)
                .quantity(10)
                .build();

        lenient().when(productClient.get(productId)).thenReturn(product);
    }

    @AfterEach
    void tearDown() {
        orderItemRepository.deleteAll();
        orderDetailsRepository.deleteAll();
        cartItemRepository.deleteAll();
        shoppingCartRepository.deleteAll();
    }

    // Integration Test 1: Full Checkout HTTP POST /orders flow using Testcontainers MongoDB
    @Test
    void createOrder_IntegrationSuccess() throws Exception {
        ShoppingCart cart = shoppingCartRepository.save(ShoppingCart.builder().userId(userId).build());
        cartItemRepository.save(CartItem.builder().shoppingCartId(cart.getId()).productId(productId).quantity(2).build());

        mockMvc.perform(post("/orders")
                .header("Authorization", token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.order_details.status").value("ORDERED"))
                .andExpect(jsonPath("$.order_details.total").value(200.0));

        // Verify order was saved in containerized MongoDB
        List<OrderDetails> orders = orderDetailsRepository.findAllByUserId(userId, null);
        assertEquals(1, orders.size());
        assertEquals(200.0, orders.get(0).getTotal());
    }

    // Integration Test 2: Fetching order by ID HTTP GET /orders/{id} flow using Testcontainers MongoDB
    @Test
    void getOrderById_IntegrationSuccess() throws Exception {
        OrderDetails savedOrder = orderDetailsRepository.save(OrderDetails.builder().userId(userId).status(OrderStatus.ORDERED).total(150.0).build());

        mockMvc.perform(get("/orders/" + savedOrder.getId())
                .header("Authorization", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedOrder.getId()))
                .andExpect(jsonPath("$.status").value("ORDERED"));
    }
}

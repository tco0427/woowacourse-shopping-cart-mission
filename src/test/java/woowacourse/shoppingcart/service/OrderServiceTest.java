package woowacourse.shoppingcart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.CustomerFixture.CUSTOMER_REQUEST;
import static woowacourse.CustomerFixture.CUSTOMER_EMAIL;
import static woowacourse.ProductFixture.SAMPLE_PRODUCT_REQUEST;
import static woowacourse.ProductFixture.SNACK_PRODUCT_REQUEST;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.dto.cart.request.CartRequest;
import woowacourse.shoppingcart.dto.cart.response.CartResponse;
import woowacourse.shoppingcart.dto.order.request.OrderRequest;
import woowacourse.shoppingcart.dto.order.response.OrderResponse;

@SpringBootTest
@Sql("/truncate.sql")
class OrderServiceTest {

    private final OrderService orderService;
    private final CartService cartService;
    private final CustomerService customerService;
    private final ProductService productService;

    @Autowired
    public OrderServiceTest(OrderService orderService, CartService cartService,
                            CustomerService customerService, ProductService productService) {
        this.orderService = orderService;
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @BeforeEach
    void setUp() {
        customerService.save(CUSTOMER_REQUEST);
    }

    @DisplayName("고객이 로그인한 상태에서 장바구니에 있는 상품들로 주문을 할 수 있다.")
    @Test
    public void saveOrder() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final CartRequest cartRequest = new CartRequest(savedProductId, 5);
        final CartResponse cartResponse = cartService.addCart(CUSTOMER_EMAIL, cartRequest);
        final int stockQuantity = SAMPLE_PRODUCT_REQUEST.getStockQuantity();

        // when
        final OrderRequest orderRequest = new OrderRequest(List.of(cartResponse.getId()));
        final Long savedOrderId = orderService.addOrder(CUSTOMER_EMAIL, orderRequest);

        // then
        final OrderResponse findOrderResponse = orderService.findOrderById(CUSTOMER_EMAIL, savedOrderId);
        assertThat(savedOrderId).isEqualTo(findOrderResponse.getId());
        assertThat(productService.findById(savedProductId).getQuantity())
                .isEqualTo(SAMPLE_PRODUCT_REQUEST.getStockQuantity() - cartResponse.getQuantity());
    }

    @DisplayName("Order의 id값을 통해서 주문 정보를 조회할 수 있다.")
    @Test
    public void findOrderById() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final CartRequest cartRequest = new CartRequest(savedProductId, 5);
        final CartResponse cartResponse = cartService.addCart(CUSTOMER_EMAIL, cartRequest);

        final OrderRequest orderRequest = new OrderRequest(List.of(cartResponse.getId()));
        final Long savedOrderId = orderService.addOrder(CUSTOMER_EMAIL, orderRequest);

        // when
        final OrderResponse findOrderResponse = orderService.findOrderById(CUSTOMER_EMAIL, savedOrderId);

        // then
        assertThat(findOrderResponse.getId()).isEqualTo(savedOrderId);
    }

    @DisplayName("고객의 email을 통해서 해당 고객이 주문한 내역 전체를 조회할 수 있다.")
    @Test
    public void findOrdersByCustomer() {
        // given
        final Long savedProductId1 = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final Long savedProductId2 = productService.save(SNACK_PRODUCT_REQUEST).getId();
        final CartRequest cartRequest1 = new CartRequest(savedProductId1, 5);
        final CartRequest cartRequest2 = new CartRequest(savedProductId2, 2);

        final CartResponse cartResponse1 = cartService.addCart(CUSTOMER_EMAIL, cartRequest1);
        final OrderRequest orderRequest1 = new OrderRequest(List.of(cartResponse1.getId()));
        final Long savedOrderId1 = orderService.addOrder(CUSTOMER_EMAIL, orderRequest1);

        final CartResponse cartResponse2 = cartService.addCart(CUSTOMER_EMAIL, cartRequest2);
        final OrderRequest orderRequest2 = new OrderRequest(List.of(cartResponse2.getId()));
        final Long savedOrderId2 = orderService.addOrder(CUSTOMER_EMAIL, orderRequest2);

        // when
        final List<OrderResponse> orderResponses = orderService.findOrdersByCustomer(CUSTOMER_EMAIL);

        // then
        assertThat(orderResponses).hasSize(2)
                .extracting("id").contains(savedOrderId1, savedOrderId2);
    }
}

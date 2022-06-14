package woowacourse.shoppingcart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.dto.CartRemovalRequest;
import woowacourse.shoppingcart.dto.CartRequest;
import woowacourse.shoppingcart.dto.CartResponse;
import woowacourse.shoppingcart.dto.CartsResponse;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.ProductRequest;
import woowacourse.shoppingcart.dto.ThumbnailImage;
import woowacourse.shoppingcart.dto.UpdateQuantityRequest;
import woowacourse.shoppingcart.exception.NotExistException;

@SpringBootTest
@Sql("/truncate.sql")
class CartServiceTest {

    private static final String CUSTOMER_EMAIL = "email@email.com";

    private static final ThumbnailImage CHOCOLATE_IMAGE =
            new ThumbnailImage("chocolateImageUrl", "chocolateImageAlt");
    private static final ThumbnailImage SNACK_IMAGE = new ThumbnailImage("snackImageUrl", "snackImageAlt");
    private static final ThumbnailImage SAMPLE_IMAGE = new ThumbnailImage("sampleUrl", "sampleAlt");

    private static final ProductRequest CHOCOLATE_PRODUCT_REQUEST =
            new ProductRequest("chocolate", 1_000, 100, CHOCOLATE_IMAGE);
    private static final ProductRequest SAMPLE_PRODUCT_REQUEST =
            new ProductRequest("sample", 10_000, 10, SAMPLE_IMAGE);
    private static final ProductRequest SNACK_PRODUCT_REQUEST =
            new ProductRequest("snack", 1_000, 100, SNACK_IMAGE);

    private final CartService cartService;
    private final CustomerService customerService;
    private final ProductService productService;

    @Autowired
    public CartServiceTest(CartService cartService, CustomerService customerService, ProductService productService) {
        this.cartService = cartService;
        this.customerService = customerService;
        this.productService = productService;
    }

    @BeforeEach
    void setUp() {
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, "password1!", "dwoo");
        customerService.save(request);
    }

    @DisplayName("장바구니에 담긴 상품에 대한 id를 통해서 단건 조회를 할 수 있다.")
    @Test
    public void findById() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final CartRequest request = new CartRequest(savedProductId, 5);
        final CartResponse cartResponse = cartService.addCart(CUSTOMER_EMAIL, request);

        // when
        final CartResponse savedResponse = cartService.findById(CUSTOMER_EMAIL, cartResponse.getId());

        // then
        assertThat(savedResponse.getId()).isEqualTo(cartResponse.getId());
    }

    @DisplayName("회원이 담은 장바구니 목록을 조회할 수 있다.")
    @Test
    public void findCartsByCustomer() {
        // given
        final Long chocolateId = productService.save(CHOCOLATE_PRODUCT_REQUEST).getId();
        final Long snackId = productService.save(SNACK_PRODUCT_REQUEST).getId();

        final CartRequest chocolateRequest = new CartRequest(chocolateId, 2);
        final CartRequest snackRequest = new CartRequest(snackId, 2);

        final CartResponse chocolateResponse = cartService.addCart(CUSTOMER_EMAIL, chocolateRequest);
        final CartResponse snackResponse = cartService.addCart(CUSTOMER_EMAIL, snackRequest);

        // when
        final CartsResponse cartsResponse = cartService.findCartsByCustomer(CUSTOMER_EMAIL);

        // then
        final List<CartResponse> carts = cartsResponse.getCarts();
        assertThat(carts).hasSize(2)
                .extracting("id", "quantity")
                .contains(
                        tuple(chocolateResponse.getId(), 2),
                        tuple(snackResponse.getId(), 2)
                );
    }

    @DisplayName("장바구니에 아이템을 추가할 수 있다.")
    @Test
    public void addCart() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final CartRequest request = new CartRequest(savedProductId, 5);

        // when
        final CartResponse savedResponse = cartService.addCart(CUSTOMER_EMAIL, request);

        // then
        final CartResponse response = cartService.findById(CUSTOMER_EMAIL, savedResponse.getId());
        assertThat(response.getId()).isEqualTo(savedResponse.getId());
    }

    @DisplayName("장바구니에 담은 아이템의 수량을 변경할 수 있다.")
    @Test
    public void updateQuantity() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final CartRequest request = new CartRequest(savedProductId, 5);
        final CartResponse savedResponse = cartService.addCart(CUSTOMER_EMAIL, request);

        // when
        final UpdateQuantityRequest updateRequest = new UpdateQuantityRequest(savedResponse.getId(), 10);
        cartService.updateQuantity(CUSTOMER_EMAIL, updateRequest);

        // then
        final CartResponse response = cartService.findById(CUSTOMER_EMAIL, savedResponse.getId());
        assertThat(response.getQuantity()).isEqualTo(10);
    }

    @DisplayName("장바구니에 담은 아이템을 삭제할 수 있다.")
    @Test
    public void deleteCart() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final CartRequest saveRequest = new CartRequest(savedProductId, 5);
        final CartResponse savedResponse = cartService.addCart(CUSTOMER_EMAIL, saveRequest);

        // when
        final CartRemovalRequest request = new CartRemovalRequest(List.of(savedResponse.getId()));
        cartService.deleteCart(CUSTOMER_EMAIL, request);

        // then
        assertThatThrownBy(() -> cartService.findById(CUSTOMER_EMAIL, savedResponse.getId()))
                .isInstanceOf(NotExistException.class);
    }
}

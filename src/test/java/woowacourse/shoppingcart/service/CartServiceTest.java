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
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.ProductRequest;
import woowacourse.shoppingcart.dto.ThumbnailImage;
import woowacourse.shoppingcart.exception.NotExistException;

@SpringBootTest
@Sql("/truncate.sql")
class CartServiceTest {

    private static final String SAMPLE_EMAIL = "email@email.com";

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
        final CustomerRequest request = new CustomerRequest(SAMPLE_EMAIL, "password1!", "dwoo");
        customerService.save(request);
    }

    @DisplayName("장바구니에 담긴 상품에 대한 id를 통해서 단건 조회를 할 수 있다.")
    @Test
    public void findById() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final Long savedId = cartService.addCart(SAMPLE_EMAIL, savedProductId, 5);

        // when
        final Cart cart = cartService.findById(savedId);

        // then
        assertThat(cart).extracting("id", "product", "quantity")
                .contains(cart.getId(), cart.getProduct(), cart.getQuantity());
    }

    @DisplayName("회원이 담은 장바구니 목록을 조회할 수 있다.")
    @Test
    public void findCartsByCustomer() {
        // given
        final Long chocolateId = productService.save(CHOCOLATE_PRODUCT_REQUEST).getId();
        final Long snackId = productService.save(SNACK_PRODUCT_REQUEST).getId();

        final Long chocolateCartId = cartService.addCart(SAMPLE_EMAIL, chocolateId, 2);
        final Long snackCartId = cartService.addCart(SAMPLE_EMAIL, snackId, 2);

        // when
        final List<Cart> carts = cartService.findCartsByCustomer(SAMPLE_EMAIL);

        // then
        assertThat(carts).hasSize(2)
                .extracting("id", "quantity")
                .contains(tuple(chocolateCartId, 2),
                        tuple(snackCartId, 2));
    }

    @DisplayName("장바구니에 아이템을 추가할 수 있다.")
    @Test
    public void addCart() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();

        // when
        final Long savedId = cartService.addCart(SAMPLE_EMAIL, savedProductId, 5);

        // then
        final Cart cart = cartService.findById(savedId);
        assertThat(cart).extracting("id", "product", "quantity")
                .contains(cart.getId(), cart.getProduct(), cart.getQuantity());
    }

    @DisplayName("장바구니에 담은 아이템의 수량을 변경할 수 있다.")
    @Test
    public void updateQuantity() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final Long savedId = cartService.addCart(SAMPLE_EMAIL, savedProductId, 5);

        // when
        cartService.updateQuantity(SAMPLE_EMAIL, savedId, 10);

        // then
        final Cart cart = cartService.findById(savedId);
        assertThat(cart.getQuantity()).isEqualTo(10);
    }

    @DisplayName("장바구니에 담은 아이템을 삭제할 수 있다.")
    @Test
    public void deleteCart() {
        // given
        final Long savedProductId = productService.save(SAMPLE_PRODUCT_REQUEST).getId();
        final Long savedId = cartService.addCart(SAMPLE_EMAIL, savedProductId, 5);

        // when
        cartService.deleteCart(SAMPLE_EMAIL, List.of(savedId));

        // then
        assertThatThrownBy(() -> cartService.findById(savedId))
                .isInstanceOf(NotExistException.class);
    }
}

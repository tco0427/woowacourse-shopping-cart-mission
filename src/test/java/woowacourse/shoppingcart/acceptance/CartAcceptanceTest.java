package woowacourse.shoppingcart.acceptance;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.CustomerFixture.SAMPLE_EMAIL;
import static woowacourse.CustomerFixture.SAMPLE_PASSWORD;
import static woowacourse.CustomerFixture.SAMPLE_USERNAME;

import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.ThumbnailImageDto;
import woowacourse.shoppingcart.dto.cart.request.CartRemovalRequest;
import woowacourse.shoppingcart.dto.cart.request.CartRequest;
import woowacourse.shoppingcart.dto.cart.request.UpdateQuantityRequest;
import woowacourse.shoppingcart.dto.cart.response.CartResponse;
import woowacourse.shoppingcart.dto.customer.request.CustomerRequest;
import woowacourse.shoppingcart.dto.product.request.ProductRequest;
import woowacourse.shoppingcart.dto.product.response.ProductResponse;

@DisplayName("장바구니 관련 기능")
public class CartAcceptanceTest extends AcceptanceTest {

    private static final String BEARER = "Bearer ";
    private static final ThumbnailImageDto CHICKEN_IMAGE =
            new ThumbnailImageDto("http://example.com/chicken.jpg", "chicken");
    private static final ThumbnailImageDto BEER_IMAGE =
            new ThumbnailImageDto("http://example.com/beer.jpg", "beer");

    private Long productId1;
    private Long productId2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        final ProductRequest chickenRequest =
                new ProductRequest("치킨", 10_000, 1_000, CHICKEN_IMAGE);
        final ProductRequest beerRequest =
                new ProductRequest("맥주", 20_000, 1_000, BEER_IMAGE);

        final ExtractableResponse<Response> chickenResponse =
                AcceptanceFixture.post(chickenRequest, "/api/products");
        final ExtractableResponse<Response> beerResponse =
                AcceptanceFixture.post(beerRequest, "/api/products");

        productId1 = extractProductId(chickenResponse);
        productId2 = extractProductId(beerResponse);
    }

    @DisplayName("로그인 되어 있는 회원은 장바구니에 상품을 추가할 수 있다.")
    @Test
    void addCartItem() {
        // given
        final Header header = initCustomer();

        final CartRequest cartRequest = new CartRequest(productId1, 10);

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.post(cartRequest, "/api/mycarts", header);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("로그인 되어 있는 회원은 자신이 장바구니에 담은 아이템 목록을 조회 할 수 있다.")
    @Test
    void getCartItems() {
        // given
        final Header header = initCustomer();

        final CartRequest cartRequest1 = new CartRequest(productId1, 10);
        final CartRequest cartRequest2 = new CartRequest(productId2, 10);
        AcceptanceFixture.post(cartRequest1, "/api/mycarts", header);
        AcceptanceFixture.post(cartRequest2, "/api/mycarts", header);

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/mycarts", header);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> resultProductIds = extractProductIds(response);
        assertThat(resultProductIds).contains(productId1, productId2);
    }

    @DisplayName("로그인 되어 있는 고객은 자신의 장바구니에서 특정 아이템에 대해서 단건 조회를 할 수 있다.")
    @Test
    public void findCartItem() {
        // given
        final Header header = initCustomer();

        final CartRequest cartRequest1 = new CartRequest(productId1, 10);
        final CartRequest cartRequest2 = new CartRequest(productId2, 10);
        final ExtractableResponse<Response> savedResponse =
                AcceptanceFixture.post(cartRequest1, "/api/mycarts", header);
        AcceptanceFixture.post(cartRequest2, "/api/mycarts", header);

        // when
        final Long cartId = extractCartId(savedResponse);
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/mycarts/" + cartId, header);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final Long findCartId = extractCartId(response);
        assertThat(findCartId).isEqualTo(cartId);
    }

    @DisplayName("로그인 되어 있는 고객은 자신의 장바구니에 담겨있는 아이템에 대해서 개수를 변경할 수 있다.")
    @Test
    public void updateCartItem() {
        // given
        final Header header = initCustomer();

        final CartRequest cartRequest = new CartRequest(productId1, 10);
        final ExtractableResponse<Response> savedResponse =
                AcceptanceFixture.post(cartRequest, "/api/mycarts", header);
        final Long cartId = extractCartId(savedResponse);

        // when
        final UpdateQuantityRequest updateQuantityRequest = new UpdateQuantityRequest(cartId, 20);
        AcceptanceFixture.patch(updateQuantityRequest, "/api/mycarts", header);

        // then
        final ExtractableResponse<Response> findResponse = AcceptanceFixture.get(
                "/api/mycarts/" + cartId, header);
        assertThat(extractCart(findResponse).getQuantity()).isEqualTo(20);
    }

    @DisplayName("장바구니 삭제")
    @Test
    void deleteCartItem() {
        // given
        final Header header = initCustomer();

        final CartRequest cartRequest = new CartRequest(productId1, 10);
        final ExtractableResponse<Response> savedResponse =
                AcceptanceFixture.post(cartRequest, "/api/mycarts", header);

        // when
        final Long cartId = extractCartId(savedResponse);
        final CartRemovalRequest request = new CartRemovalRequest(List.of(cartId));

        final ExtractableResponse<Response> response =
                AcceptanceFixture.delete(request, "/api/mycarts", header);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Header initCustomer() {
        final CustomerRequest request = new CustomerRequest(SAMPLE_EMAIL, SAMPLE_PASSWORD, SAMPLE_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest(SAMPLE_EMAIL, SAMPLE_PASSWORD);
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");
        final String accessToken = extractAccessToken(loginResponse);
        return createHeader(accessToken);
    }

    private String extractAccessToken(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", TokenResponse.class)
                .getAccessToken();
    }

    private Header createHeader(String accessToken) {
        return new Header("Authorization", BEARER + accessToken);
    }

    private CartResponse extractCart(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", CartResponse.class);
    }

    private Long extractProductId(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", ProductResponse.class)
                .getId();
    }

    private Long extractCartId(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", CartResponse.class)
                .getId();
    }

    private List<Long> extractProductIds(ExtractableResponse<Response> response) {
        final List<CartResponse> cartResponses = response.jsonPath().getList(".", CartResponse.class);
        return cartResponses.stream()
                .map(CartResponse::getId)
                .collect(toList());
    }
}

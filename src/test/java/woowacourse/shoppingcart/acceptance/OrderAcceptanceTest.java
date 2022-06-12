package woowacourse.shoppingcart.acceptance;

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
import woowacourse.shoppingcart.dto.cart.request.CartRequest;
import woowacourse.shoppingcart.dto.cart.response.CartResponse;
import woowacourse.shoppingcart.dto.customer.request.CustomerRequest;
import woowacourse.shoppingcart.dto.order.request.OrderRequest;
import woowacourse.shoppingcart.dto.order.response.OrderResponse;
import woowacourse.shoppingcart.dto.product.request.ProductRequest;
import woowacourse.shoppingcart.dto.product.response.ProductResponse;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest {

    private static final String BEARER = "Bearer ";
    private static final ThumbnailImageDto CHICKEN_IMAGE =
            new ThumbnailImageDto("http://example.com/chicken.jpg", "chicken");
    private static final ThumbnailImageDto BEER_IMAGE =
            new ThumbnailImageDto("http://example.com/beer.jpg", "beer");

    private Long cartId1;
    private Long cartId2;
    private Header header;

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

        Long productId1 = extractProductId(chickenResponse);
        Long productId2 = extractProductId(beerResponse);

        header = initCustomer();

        final ExtractableResponse<Response> cartResponse1 =
                AcceptanceFixture.post(new CartRequest(productId1, 10), "/api/mycarts", header);

        final ExtractableResponse<Response> cartResponse2 =
                AcceptanceFixture.post(new CartRequest(productId2, 10), "/api/mycarts", header);

        cartId1 = extractCartId(cartResponse1);
        cartId2 = extractCartId(cartResponse2);
    }

    @DisplayName("고객은 로그인 한 이후에 장바구니에 담긴 상품 정보로 주문을 할 수 있다.")
    @Test
    void addOrder() {
        // given
        final OrderRequest orderRequest = new OrderRequest(List.of(cartId1, cartId2));

        // when
        final ExtractableResponse<Response> response =
                AcceptanceFixture.post(orderRequest, "/api/myorders", header);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("고객은 자신이 주문했던 주문 내역을 조회할 수 있다.")
    @Test
    void findOrders() {
        // given
        final OrderRequest orderRequest = new OrderRequest(List.of(cartId1, cartId2));
        final ExtractableResponse<Response> orderResponse1 =
                AcceptanceFixture.post(orderRequest, "/api/myorders", header);
        final ExtractableResponse<Response> orderResponse2 =
                AcceptanceFixture.post(orderRequest, "/api/myorders", header);

        Long orderId1 = extractOrderIdFromLocation(orderResponse1);
        Long orderId2 = extractOrderIdFromLocation(orderResponse2);

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/myorders", header);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        final List<OrderResponse> orderResponses = extractOrdersResponse(response);

        assertThat(orderResponses).hasSize(2)
                        .extracting("id")
                                .contains(orderId1, orderId2);
    }

    @DisplayName("고객은 자신이 주문했던 주문 목록 중에서 주문 하나에 대해서 단건 조회를 할 수 있다.")
    @Test
    void findOrder() {
        // given
        final OrderRequest orderRequest = new OrderRequest(List.of(cartId1, cartId2));
        final ExtractableResponse<Response> orderResponse =
                AcceptanceFixture.post(orderRequest, "/api/myorders", header);

        Long orderId = extractOrderIdFromLocation(orderResponse);

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/myorders/" + orderId, header);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extractOrderId(response)).isEqualTo(orderId);
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

    private List<OrderResponse> extractOrdersResponse(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", OrderResponse.class);
    }

    private Long extractOrderIdFromLocation(ExtractableResponse<Response> response) {
        return Long.parseLong(response.header("Location").split("/myorders/")[1]);
    }

    private Long extractOrderId(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", OrderResponse.class)
                .getId();
    }
}

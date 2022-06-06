package woowacourse.shoppingcart.acceptance;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowacourse.shoppingcart.domain.Image;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.ProductRequest;
import woowacourse.shoppingcart.dto.ProductResponse;
import woowacourse.shoppingcart.dto.ProductResponses;
import woowacourse.shoppingcart.dto.ThumbnailImage;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    private static final ThumbnailImage CHICKEN_IMAGE =
            new ThumbnailImage("http://example.com/chicken.jpg", "chicken");
    private static final ThumbnailImage BEER_IMAGE = new ThumbnailImage("http://example.com/beer.jpg", "beer");

    @DisplayName("상품 정보를 가지고 상품 추가를 요청하면 상품이 저장된다.")
    @Test
    void addProduct() {
        // given
        ProductRequest productRequest =
                new ProductRequest("치킨", 10_000, 1_000, CHICKEN_IMAGE);

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.post(productRequest, "/api/products");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("상품 목록을 요청하면 저장된 상품들의 정보를 반환하여 준다.")
    @Test
    void getProducts() {
        // given
        ProductRequest chickenRequest =
                new ProductRequest("치킨", 10_000, 1_000, CHICKEN_IMAGE);
        final ExtractableResponse<Response> chickenResponse =
                AcceptanceFixture.post(chickenRequest, "/api/products");

        ProductRequest beerRequest =
                new ProductRequest("치킨", 10_000, 1_000, CHICKEN_IMAGE);
        final ExtractableResponse<Response> beerResponse = AcceptanceFixture.post(beerRequest, "/api/products");

        final Long chickenId = extractId(chickenResponse);
        final Long beerId = extractId(beerResponse);

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/products");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> resultProductIds = extractIds(response);
        assertThat(resultProductIds).contains(chickenId, beerId);
    }

    @DisplayName("저장된 상품의 id와 함께 상품 조회를 요청하면 해당 상품에 단건 조회가 가능하다.")
    @Test
    void getProduct() {
        // given
        ProductRequest request =
                new ProductRequest("치킨", 10_000, 1_000, CHICKEN_IMAGE);
        final ExtractableResponse<Response> chickenResponse =
                AcceptanceFixture.post(request, "/api/products");

        final Long productId = extractId(chickenResponse);

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.get("/api/products/" + productId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(extractId(response)).isEqualTo(productId);
    }

    @DisplayName("저장되어 있는 상품의 id를 가지고 해당 상품을 삭제할 수 있다.")
    @Test
    void deleteProduct() {
        // given
        ProductRequest request =
                new ProductRequest("치킨", 10_000, 1_000, CHICKEN_IMAGE);
        final ExtractableResponse<Response> chickenResponse =
                AcceptanceFixture.post(request, "/api/products");

        final Long productId = extractId(chickenResponse);

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.delete("/api/products/" + productId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", ProductResponse.class)
                .getId();
    }

    private List<Long> extractIds(ExtractableResponse<Response> response) {
        final ProductResponses responses = response.jsonPath().getObject(".", ProductResponses.class);
        return responses.getProductResponses().stream()
                .map(ProductResponse::getId)
                .collect(toList());
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, int price, int quantity, Image image) {
        Product productRequest = new Product(name, price, quantity, image);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static Long 상품_등록되어_있음(String name, int price, int quantity, Image image) {
        ExtractableResponse<Response> response = 상품_등록_요청(name, price, quantity, image);
        return Long.parseLong(response.header("Location").split("/products/")[1]);
    }
}

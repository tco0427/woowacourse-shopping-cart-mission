package woowacourse.shoppingcart.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.CustomerResponse;
import woowacourse.shoppingcart.dto.CustomerUpdateRequest;

@DisplayName("회원 관련 기능")
public class CustomerAcceptanceTest extends AcceptanceTest {

    private static final String BEARER = "Bearer ";

    @DisplayName("email, password, username 을 통해서 회원가입을 할 수 있다.")
    @Test
    void addCustomer() {
        // given
        final CustomerRequest request =
                new CustomerRequest("email@email.com", "password1!", "azpi");

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.post(request, "/api/customers");

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("내 정보 조회")
    @Test
    void getMe() {
        // given
        final CustomerRequest request =
                new CustomerRequest("email@email.com", "password1!", "azpi");
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest("email@email.com", "password1!");
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");
        final String accessToken = extractAccessToken(loginResponse);

        // when
        Header header = new Header("Authorization", BEARER + accessToken);
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/customers/me", header);

        final CustomerResponse customerResponse = extractCustomer(response);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(customerResponse)
                .extracting("email", "username")
                .containsExactly(request.getEmail(), request.getUsername());
    }

    @DisplayName("내 정보 수정")
    @Test
    void updateMe() {
        // given
        final CustomerRequest request =
                new CustomerRequest("email@email.com", "password1!", "azpi");
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest("email@email.com", "password1!");
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");
        final String accessToken = extractAccessToken(loginResponse);

        // when
        final CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("dwoo");
        Header header = new Header("Authorization", BEARER + accessToken);
        final ExtractableResponse<Response> response =
                AcceptanceFixture.patch(updateRequest,"/api/customers/me?target=generalInfo", header);

        final CustomerResponse customerResponse = extractCustomer(response);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(customerResponse)
                .extracting("email", "username")
                .containsExactly(request.getEmail(), updateRequest.getUsername());
    }

    @DisplayName("회원탈퇴")
    @Test
    void deleteMe() {
    }

    private String extractAccessToken(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", TokenResponse.class)
                .getAccessToken();
    }

    private CustomerResponse extractCustomer(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", CustomerResponse.class);
    }
}

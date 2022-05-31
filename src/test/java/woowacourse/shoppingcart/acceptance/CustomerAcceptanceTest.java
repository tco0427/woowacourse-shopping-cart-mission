package woowacourse.shoppingcart.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.CustomerResponse;

@DisplayName("회원 관련 기능")
public class CustomerAcceptanceTest extends AcceptanceTest {

    @DisplayName("email, password, username 을 통해서 회원가입을 할 수 있다.")
    @Test
    void addCustomer() {
        // given
        final CustomerRequest request =
                new CustomerRequest("email@email.com", "password1!", "azpi");

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.post(request, "/api/customers");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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
        Header header = new Header("Authorization", "Bearer " + accessToken);
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/customers/me", header);

        final CustomerResponse customerResponse = extractCustomer(response);

        // then
        assertThat(customerResponse)
                .extracting("email", "username")
                .containsExactly(request.getEmail(), request.getUsername());
    }

    @DisplayName("내 정보 수정")
    @Test
    void updateMe() {
    }

    @DisplayName("회원탈퇴")
    @Test
    void deleteMe() {
    }

    private String extractAccessToken(ExtractableResponse<Response> loginResponse) {
        return loginResponse.jsonPath()
                .getObject(".", TokenResponse.class)
                .getAccessToken();
    }

    private CustomerResponse extractCustomer(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", CustomerResponse.class);
    }
}

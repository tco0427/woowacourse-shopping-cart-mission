package woowacourse.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.acceptance.AcceptanceFixture;
import woowacourse.shoppingcart.acceptance.AcceptanceTest;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.CustomerResponse;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String BEARER = "Bearer ";
    private static final int LOGIN_FAIL = 2001;

    @DisplayName("Bearer Auth 로그인 성공")
    @Test
    void myInfoWithBearerAuth() {
        // given
        final CustomerRequest request =
                new CustomerRequest("email@email.com", "password1!", "dwoo");
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest(request.getEmail(), request.getPassword());
        final ExtractableResponse<Response> loginResponse = AcceptanceFixture.post(tokenRequest, "/api/auth/login");

        // when
        final String accessToken = extractAccessToken(loginResponse);

        final Header header = new Header("Authorization", BEARER + accessToken);
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/customers/me", header);
        final CustomerResponse customerResponse = extractCustomer(response);

        // then
        assertThat(loginResponse.statusCode()).isEqualTo(OK.value());
        assertThat(customerResponse)
                .extracting("email", "username")
                .containsExactly(request.getEmail(), request.getUsername());
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @ParameterizedTest
    @CsvSource(value = {"email@email.com, password2!", "email2@email.com, password1!"})
    void myInfoWithBadBearerAuth(String email, String password) {
        // given
        final CustomerRequest request =
                new CustomerRequest("email@email.com", "password1!", "dwoo");
        AcceptanceFixture.post(request, "/api/customers");

        // when
        final TokenRequest tokenRequest = new TokenRequest(email, password);
        final ExtractableResponse<Response> response = AcceptanceFixture.post(tokenRequest, "/api/auth/login");

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(extractErrorCode(response)).isEqualTo(LOGIN_FAIL);
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        // 유효하지 않은 토큰을 사용하여 내 정보 조회를 요청하면

        // then
        // 내 정보 조회 요청이 거부된다
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

    private int extractErrorCode(ExtractableResponse<Response> response) {
        return response.jsonPath().getInt("errorCode");
    }
}

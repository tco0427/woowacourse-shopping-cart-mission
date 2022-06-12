package woowacourse.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static woowacourse.CustomerFixture.CUSTOMER_EMAIL;
import static woowacourse.CustomerFixture.CUSTOMER_PASSWORD;
import static woowacourse.CustomerFixture.CUSTOMER_USERNAME;

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
import woowacourse.shoppingcart.dto.customer.request.CustomerRequest;
import woowacourse.shoppingcart.dto.customer.response.CustomerResponse;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String BEARER = "Bearer ";
    private static final String INVALID_TOKEN_HEADER = "invalidTokenHeader";
    private static final int LOGIN_FAIL = 2001;
    private static final int INVALID_TOKEN = 3002;

    @DisplayName("이메일, 비밀번호, 닉네임으로 회원가입을 한 이후 동일한 이메일, 비밀번호로 로그인 시 로그인에 성공한다.")
    @Test
    void myInfoWithBearerAuth() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
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

    @DisplayName("회원가입시 입력한 비밀번호와 다른경우 혹은 가입되지 않은 회원인 경우 로그인에 실패한다.")
    @ParameterizedTest
    @CsvSource(value = {"email@email.com, invalidpwd1!", "invalidEmail@email.com, password1!"})
    void myInfoWithBadBearerAuth(String email, String password) {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        // when
        final TokenRequest tokenRequest = new TokenRequest(email, password);
        final ExtractableResponse<Response> response = AcceptanceFixture.post(tokenRequest, "/api/auth/login");

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(extractErrorCode(response)).isEqualTo(LOGIN_FAIL);
    }

    @DisplayName("유효하지 않은 토큰으로 내 정보를 조회하려고 시도하면 실패한다.")
    @Test
    void myInfoWithWrongBearerAuth() {
        // when
        final Header header = new Header("Authorization", BEARER + INVALID_TOKEN_HEADER);
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/customers/me", header);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
        assertThat(extractErrorCode(response)).isEqualTo(INVALID_TOKEN);
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

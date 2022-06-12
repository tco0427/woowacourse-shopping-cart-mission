package woowacourse.shoppingcart.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
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
import woowacourse.shoppingcart.dto.customer.request.ChangePasswordRequest;
import woowacourse.shoppingcart.dto.customer.request.CustomerDeletionRequest;
import woowacourse.shoppingcart.dto.customer.request.CustomerRequest;
import woowacourse.shoppingcart.dto.customer.response.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.request.CustomerUpdateRequest;

@DisplayName("회원 관련 기능")
public class CustomerAcceptanceTest extends AcceptanceTest {

    private static final String BEARER = "Bearer ";
    private static final int DUPLICATE_EMAIL = 1001;
    private static final int INCORRECT_PASSWORD = 3001;

    @DisplayName("email, password, username을 가지고 회원가입을 요청하면 해당 정보로 고객 정보를 생성한다.")
    @Test
    void addCustomer() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.post(request, "/api/customers");

        // then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
        assertThat(response.header("Location")).isNotBlank().isEqualTo("/login");
    }

    @DisplayName("회원가입 이후에 회원가입시에 사용한 email과 password로 로그인할 수 있다.")
    @Test
    void login() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        // when
        final TokenRequest tokenRequest = new TokenRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");

        // then
        assertThat(loginResponse.statusCode()).isEqualTo(OK.value());

        final String accessToken = extractAccessToken(loginResponse);
        final ExtractableResponse<Response> response =
                AcceptanceFixture.get("/api/customers/me", createHeader(accessToken));
        final CustomerResponse customerResponse = extractCustomer(response);
        assertThat(customerResponse).extracting("email", "username")
                .contains(CUSTOMER_EMAIL, CUSTOMER_USERNAME);
    }

    @DisplayName("로그인 한 이후에 내 정보(email, username)을 조회할 수 있다.")
    @Test
    void getMe() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");
        final String accessToken = extractAccessToken(loginResponse);

        // when
        Header header = createHeader(accessToken);
        final ExtractableResponse<Response> response = AcceptanceFixture.get("/api/customers/me", header);

        final CustomerResponse customerResponse = extractCustomer(response);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(customerResponse)
                .extracting("email", "username")
                .containsExactly(request.getEmail(), request.getUsername());
    }

    @DisplayName("회원가입을 하고, 로그인을 해서 인증을 하고 나면 내 정보(username)을 수정할 수 있다.")
    @Test
    void updateMe() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");
        final String accessToken = extractAccessToken(loginResponse);

        // when
        final CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("dwoo");
        Header header = createHeader(accessToken);
        final ExtractableResponse<Response> response =
                AcceptanceFixture.patch(updateRequest,"/api/customers/me?target=generalInfo", header);

        final CustomerResponse customerResponse = extractCustomer(response);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(customerResponse)
                .extracting("email", "username")
                .containsExactly(request.getEmail(), updateRequest.getUsername());
    }

    @DisplayName("회원가입을 하고, 로그인을 해서 인증을 하고 나면 현재 비밀번호와 새 비밀번호를 입력하여 비밀번호 변경을 할 수 있다.")
    @Test
    void changePassword() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");
        final String accessToken = extractAccessToken(loginResponse);

        // when
        final ChangePasswordRequest changePasswordRequest =
                new ChangePasswordRequest(request.getPassword(), "newpwd1!");
        Header header = createHeader(accessToken);
        final ExtractableResponse<Response> response =
                AcceptanceFixture.patch(changePasswordRequest,"/api/customers/me?target=password", header);

        // then
        assertThat(response.statusCode()).isEqualTo(OK.value());
        assertThat(response.header("Location")).isNotBlank().isEqualTo("/login");
    }

    @DisplayName("로그인을 한 이후에 현재 비밀번호와 함께 탈퇴 요청을 하면 탈퇴를 할 수 있다.")
    @Test
    void deleteMe() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");
        final String accessToken = extractAccessToken(loginResponse);

        // when
        final CustomerDeletionRequest customerDeletionRequest =
                new CustomerDeletionRequest(request.getPassword());
        Header header = createHeader(accessToken);
        final ExtractableResponse<Response> response =
                AcceptanceFixture.delete(customerDeletionRequest,"/api/customers/me", header);

        // then
        assertThat(response.statusCode()).isEqualTo(NO_CONTENT.value());
        assertThat(response.header("Location")).isNotBlank().isEqualTo("/");
    }

    @DisplayName("이미 등록되어 있는 이메일로는 회원가입이 불가능하다.")
    @Test
    public void duplicateEmail() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.post(request, "/api/customers");

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(extractErrorCode(response)).isEqualTo(DUPLICATE_EMAIL);
    }

    @DisplayName("잘못된 형식으로 회원가입 요청을 한 경우 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource(value = {"email, password1!, azpi, 4001",
            "email@email.com, pass1!, azpi, 4002",
            "email@email.com, password1!, abcdefghijk, 4003"})
    public void invalidEmail(String email, String password, String username, int errorCode) {
        // given
        final CustomerRequest request =
                new CustomerRequest(email, password, username);

        // when
        final ExtractableResponse<Response> response = AcceptanceFixture.post(request, "/api/customers");

        // then
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(extractErrorCode(response)).isEqualTo(errorCode);
    }

    @DisplayName("기존 패스워드와 불일치하는 경우 비밀번호 변경이 불가능하다.")
    @Test
    public void invalidChangePassword() {
        // given
        final CustomerRequest request = new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
        AcceptanceFixture.post(request, "/api/customers");

        final TokenRequest tokenRequest = new TokenRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD);
        final ExtractableResponse<Response> loginResponse =
                AcceptanceFixture.post(tokenRequest, "/api/auth/login");
        final String accessToken = extractAccessToken(loginResponse);

        // when
        final ChangePasswordRequest changePasswordRequest =
                new ChangePasswordRequest("incorrect1!", "newpwd1!");
        Header header = createHeader(accessToken);
        final ExtractableResponse<Response> response =
                AcceptanceFixture.patch(changePasswordRequest,"/api/customers/me?target=password", header);

        // then
        assertThat(response.statusCode()).isEqualTo(UNAUTHORIZED.value());
        assertThat(extractErrorCode(response)).isEqualTo(INCORRECT_PASSWORD);
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

    private Header createHeader(String accessToken) {
        return new Header("Authorization", BEARER + accessToken);
    }
}

package woowacourse.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.CustomerFixture.SAMPLE_EMAIL;
import static woowacourse.CustomerFixture.SAMPLE_PASSWORD;
import static woowacourse.CustomerFixture.SAMPLE_USERNAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.service.CustomerService;

@SpringBootTest
@Sql("/truncate.sql")
class AuthServiceTest {

    private final AuthService authService;
    private final CustomerService customerService;

    @Autowired
    public AuthServiceTest(AuthService authService, CustomerService customerService) {
        this.authService = authService;
        this.customerService = customerService;
    }

    @DisplayName("email과 password를 통해 로그인을 하면 토큰을 발급해준다.")
    @Test
    public void createToken() {
        // given
        saveSampleCustomer();
        final TokenRequest tokenRequest = new TokenRequest(SAMPLE_EMAIL, SAMPLE_PASSWORD);

        // when
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);

        // then
        assertThat(tokenResponse).isNotNull();
    }

    @DisplayName("토큰을 받아서 유저 정보(email)를 추출해낼 수 있다.")
    @Test
    public void findCustomerByToken() {
        // given
        saveSampleCustomer();
        final TokenRequest tokenRequest = new TokenRequest(SAMPLE_EMAIL, SAMPLE_PASSWORD);
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);

        // when
        final String accessToken = tokenResponse.getAccessToken();
        final String email = authService.findCustomerByToken(accessToken);

        // then
        assertThat(email).isEqualTo(SAMPLE_EMAIL);
    }

    private void saveSampleCustomer() {
        final CustomerRequest customerRequest =
                new CustomerRequest(SAMPLE_EMAIL, SAMPLE_PASSWORD, SAMPLE_USERNAME);
        customerService.save(customerRequest);
    }
}

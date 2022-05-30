package woowacourse.auth.service;

import static org.assertj.core.api.Assertions.assertThat;

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
        saveCustomer();
        final TokenRequest tokenRequest = new TokenRequest("email@email.com", "password1!");

        // when
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);

        // then
        assertThat(tokenResponse).isNotNull();
    }

    private void saveCustomer() {
        final CustomerRequest customerRequest =
                new CustomerRequest("email@email.com", "password1!", "dwoo");
        customerService.save(customerRequest);
    }
}

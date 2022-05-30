package woowacourse.shoppingcart.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.CustomerResponse;

@SpringBootTest
@Sql("/truncate.sql")
class CustomerServiceTest {

    private final CustomerService customerService;

    @Autowired
    public CustomerServiceTest(CustomerService customerService) {
        this.customerService = customerService;
    }

    @DisplayName("Customer 정보를 저장할 수 있다.")
    @Test
    public void save() {
        // given
        final CustomerRequest request = new CustomerRequest("email@email.com", "password1!", "azpi");

        // when
        customerService.save(request);

        // then
        final CustomerResponse response = customerService.findByEmail(request.getEmail());
        assertThat(response)
                .extracting("email", "username")
                .contains(request.getEmail(), request.getUsername());
    }

    @DisplayName("email 을 통해서 해당 Customer를 조회할 수 있다.")
    @Test
    public void findByEmail() {
        // given
        final CustomerRequest request = new CustomerRequest("email@email.com", "password1!", "azpi");
        customerService.save(request);

        // when
        final CustomerResponse response = customerService.findByEmail(request.getEmail());

        // then
        assertThat(response)
                .extracting("email", "username")
                .contains(request.getEmail(), request.getUsername());
    }
}

package woowacourse.shoppingcart.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowacourse.shoppingcart.domain.customer.Customer;

class CustomerTest {

    @DisplayName("customer 는 email, password, username 를 통해 생성할 수 있다.")
    @Test
    void createCustomer() {
        assertDoesNotThrow(() -> new Customer("email@email.com", "password!2", "user"));
    }
    
}

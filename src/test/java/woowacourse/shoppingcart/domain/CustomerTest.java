package woowacourse.shoppingcart.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowacourse.shoppingcart.domain.customer.Customer;

class CustomerTest {

    @DisplayName("customer 는 email, password, username 을 갖는다.")
    @Test
    void createCustomer() {
        assertDoesNotThrow(() -> new Customer("email@email.com", "password!2", "user"));
    }
    
}

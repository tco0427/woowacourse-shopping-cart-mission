package woowacourse.shoppingcart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static woowacourse.CustomerFixture.CUSTOMER_EMAIL;
import static woowacourse.CustomerFixture.CUSTOMER_USERNAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.exception.InvalidPasswordException;

class CustomerTest {

    @DisplayName("customer 는 email, password, username 를 통해 생성할 수 있다.")
    @Test
    void createCustomer() {
        assertDoesNotThrow(() -> new Customer("email@email.com", "password!2", "user"));
    }

    @DisplayName("customer 는 입력된 비밀번호와 기존 비밀번호와의 일치여부를 확인할 수 있다.")
    @Test
    public void checkCorrectPassword() {
        // given
        final Customer customer = new Customer(CUSTOMER_EMAIL, "password1!", CUSTOMER_USERNAME);

        // when & then
        assertThatThrownBy(() -> customer.checkCorrectPassword("incorrect1!"))
                        .isInstanceOf(InvalidPasswordException.class);
        assertDoesNotThrow(() -> customer.checkCorrectPassword(customer.getPassword()));
    }
}

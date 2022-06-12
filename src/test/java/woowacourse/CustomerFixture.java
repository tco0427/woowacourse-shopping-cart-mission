package woowacourse;

import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.customer.request.CustomerRequest;

public class CustomerFixture {

    public static final String CUSTOMER_EMAIL = "email@email.com";
    public static final String CUSTOMER_PASSWORD = "password1!";
    public static final String CUSTOMER_USERNAME = "dwoo";
    public static final Customer SAMPLE_CUSTOMER = new Customer(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);
    public static final CustomerRequest CUSTOMER_REQUEST =
            new CustomerRequest(CUSTOMER_EMAIL, CUSTOMER_PASSWORD, CUSTOMER_USERNAME);

    private CustomerFixture() {
    }
}

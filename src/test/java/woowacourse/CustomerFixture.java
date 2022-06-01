package woowacourse;

import woowacourse.shoppingcart.domain.customer.Customer;

public class CustomerFixture {

    public static final String SAMPLE_EMAIL = "email@email.com";
    public static final String SAMPLE_PASSWORD = "password1!";
    public static final String SAMPLE_USERNAME = "dwoo";
    public static final Customer SAMPLE_CUSTOMER = new Customer(SAMPLE_EMAIL, SAMPLE_PASSWORD, SAMPLE_USERNAME);

    private CustomerFixture() {
    }
}

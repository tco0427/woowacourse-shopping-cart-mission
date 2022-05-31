package woowacourse.shoppingcart.dto;

import woowacourse.shoppingcart.domain.customer.Customer;

public class CustomerResponse {

    private final String email;
    private final String username;

    public CustomerResponse(Customer customer) {
        this(customer.getEmail(), customer.getUsername());
    }

    public CustomerResponse(String email, String username) {
        this.email = email;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}

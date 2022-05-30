package woowacourse.shoppingcart.dto;

import woowacourse.shoppingcart.domain.Customer;

public class CustomerResponse {

    private final String email;
    private final String password;
    private final String username;

    public CustomerResponse(Customer customer) {
        this(customer.getEmail(), customer.getPassword(), customer.getUsername());
    }

    public CustomerResponse(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}

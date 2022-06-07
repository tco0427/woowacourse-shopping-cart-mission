package woowacourse.shoppingcart.dto.customer.response;

public class CustomerResponse {

    private final String email;
    private final String username;

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

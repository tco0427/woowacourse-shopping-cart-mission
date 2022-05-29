package woowacourse.shoppingcart.domain;

public class Customer {
    private final String email;
    private final String password;
    private final String username;

    public Customer(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}

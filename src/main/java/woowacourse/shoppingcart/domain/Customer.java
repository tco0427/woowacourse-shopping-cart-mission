package woowacourse.shoppingcart.domain;

import woowacourse.shoppingcart.exception.InvalidCustomerException;

public class Customer {

    private final Long id;
    private final String email;
    private final String password;
    private final String username;

    public Customer(String email, String password, String username) {
        this(null, email, password, username);
    }

    public Customer(Long id, String email, String password, String username) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public void checkPassword(String password) {
        if (!this.password.equals(password)) {
            throw new InvalidCustomerException("고객 정보가 일치하지 않습니다.");
        }
    }

    public Long getId() {
        return id;
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

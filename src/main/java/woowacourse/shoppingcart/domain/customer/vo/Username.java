package woowacourse.shoppingcart.domain.customer.vo;

import java.util.Objects;

public class Username {

    private final String username;

    private Username(String username) {
        this.username = username;
    }

    public static Username from(String username) {
        validateSize(username);
        return new Username(username);
    }

    private static void validateSize(String username) {
        if (username.isBlank() || username.length() > 10) {
            throw new IllegalArgumentException("Invalid Username");
        }
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Username username1 = (Username) o;
        return Objects.equals(username, username1.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}

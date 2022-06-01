package woowacourse.shoppingcart.domain.customer.vo;

import java.util.Objects;
import java.util.regex.Pattern;
import woowacourse.shoppingcart.exception.InvalidPasswordException;

public class Password {

    private static final String REGEX = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";

    private final String password;

    private Password(String password) {
        this.password = password;
    }

    public static Password from(String password) {
        validate(password);
        return new Password(password);
    }

    private static void validate(String password) {
        checkSize(password);
        checkArgument(Pattern.matches(REGEX, password));
    }

    private static void checkSize(String password) {
        if (password.isBlank() || password.length() < 8 || password.length() > 12) {
            throw new IllegalArgumentException("Invalid Password");
        }
    }

    private static void checkArgument(boolean matches) {
        if (!matches) {
            throw new IllegalArgumentException("Invalid Password");
        }
    }

    public void checkSamePassword(String password) {
        if (!this.password.equals(password)) {
            throw new InvalidPasswordException("Incorrect Password");
        }
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}

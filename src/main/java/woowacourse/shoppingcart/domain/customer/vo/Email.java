package woowacourse.shoppingcart.domain.customer.vo;

import java.util.Objects;
import java.util.regex.Pattern;

public class Email {

    private static final String REGEX = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private final String email;

    private Email(String email) {
        this.email = email;
    }

    public static Email from(String email) {
        validate(email);
        return new Email(email);
    }

    private static void validate(String email) {
        checkSize(email);
        checkArgument(Pattern.matches(REGEX, email));
    }

    private static void checkSize(String email) {
        if (email.isBlank() || email.length() > 255) {
            throw new IllegalArgumentException("Invalid Email");
        }
    }

    private static void checkArgument(boolean matches) {
        if (!matches) {
            throw new IllegalArgumentException("Invalid Email");
        }
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Email email1 = (Email) o;
        return Objects.equals(email, email1.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}

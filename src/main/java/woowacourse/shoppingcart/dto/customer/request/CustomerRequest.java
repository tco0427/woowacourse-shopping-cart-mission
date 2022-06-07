package woowacourse.shoppingcart.dto.customer.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerRequest {

    private static final String INVALID_EMAIL = "Invalid Email";
    private static final String INVALID_PASSWORD = "Invalid Password";
    private static final String INVALID_USERNAME = "Invalid Username";

    @NotBlank(message = INVALID_EMAIL)
    @Email(message = INVALID_EMAIL, regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @NotBlank(message = INVALID_PASSWORD)
    @Pattern(message = INVALID_PASSWORD, regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

    @NotBlank(message = INVALID_USERNAME)
    @Size(message = INVALID_USERNAME, max = 10)
    private String username;

    private CustomerRequest() {
    }

    public CustomerRequest(String email, String password, String username) {
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

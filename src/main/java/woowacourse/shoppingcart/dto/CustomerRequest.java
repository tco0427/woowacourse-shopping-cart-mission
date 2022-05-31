package woowacourse.shoppingcart.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerRequest {

    private static final String INVALID_VALUE = "Invalid Value";
    private static final String INVALID_PASSWORD = "Invalid Password";

    @NotBlank(message = INVALID_VALUE)
    @Email(message = INVALID_VALUE, regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @NotBlank(message = INVALID_VALUE)
    @Min(value = 8,message = INVALID_PASSWORD) @Max(value = 12, message = INVALID_PASSWORD)
    @Pattern(message = INVALID_PASSWORD, regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

    @NotBlank(message = INVALID_VALUE)
    private String username;

    public CustomerRequest() {
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

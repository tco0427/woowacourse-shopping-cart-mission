package woowacourse.shoppingcart.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerRequest {

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$")
    private String email;

    @NotBlank
    @Pattern(regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

    @NotBlank
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

package woowacourse.shoppingcart.dto.customer.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerDeletionRequest {

    private static final String INVALID_PASSWORD = "Invalid Password";

    @NotBlank(message = INVALID_PASSWORD)
    @Pattern(message = INVALID_PASSWORD, regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

    private CustomerDeletionRequest() {
    }

    public CustomerDeletionRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

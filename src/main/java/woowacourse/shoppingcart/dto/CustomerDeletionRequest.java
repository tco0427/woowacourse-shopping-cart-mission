package woowacourse.shoppingcart.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerDeletionRequest {

    private static final String INVALID_VALUE = "Invalid Value";
    private static final String INVALID_PASSWORD = "Invalid Password";

    @NotBlank(message = INVALID_VALUE)
    @Min(value = 8,message = INVALID_PASSWORD) @Max(value = 12, message = INVALID_PASSWORD)
    @Pattern(message = INVALID_PASSWORD, regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String password;

    public CustomerDeletionRequest() {
    }

    public CustomerDeletionRequest(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}

package woowacourse.shoppingcart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerDeletionRequest {

    @NotBlank
    @Pattern(regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
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

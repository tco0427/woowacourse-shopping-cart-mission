package woowacourse.shoppingcart.dto.customer.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ChangePasswordRequest {

    private static final String INVALID_PASSWORD = "Invalid Password";

    private String oldPassword;

    @NotBlank(message = INVALID_PASSWORD)
    @Pattern(message = INVALID_PASSWORD, regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String newPassword;

    private ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}

package woowacourse.shoppingcart.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class ChangePasswordRequest {

    private static final String INVALID_VALUE = "Invalid Value";
    private static final String INVALID_PASSWORD = "Invalid Password";

    @NotBlank(message = INVALID_VALUE)
    @Min(value = 8,message = INVALID_PASSWORD) @Max(value = 12, message = INVALID_PASSWORD)
    @Pattern(message = INVALID_PASSWORD, regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String oldPassword;

    @NotBlank(message = INVALID_VALUE)
    @Min(value = 8,message = INVALID_PASSWORD) @Max(value = 12, message = INVALID_PASSWORD)
    @Pattern(message = INVALID_PASSWORD, regexp = "^.*(?=^.{8,12}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")
    private String newPassword;

    public ChangePasswordRequest() {
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

package woowacourse.shoppingcart.dto;

import javax.validation.constraints.NotBlank;

public class CustomerUpdateRequest {

    private static final String INVALID_VALUE = "Invalid Value";

    @NotBlank(message = INVALID_VALUE)
    private String username;

    public CustomerUpdateRequest() {
    }

    public CustomerUpdateRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

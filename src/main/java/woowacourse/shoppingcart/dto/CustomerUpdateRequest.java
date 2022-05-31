package woowacourse.shoppingcart.dto;

import javax.validation.constraints.NotBlank;

public class CustomerUpdateRequest {

    @NotBlank
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

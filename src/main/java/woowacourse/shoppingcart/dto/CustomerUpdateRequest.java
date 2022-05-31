package woowacourse.shoppingcart.dto;

public class CustomerUpdateRequest {

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

package woowacourse.shoppingcart.dto;

public class CustomerDeletionRequest {

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

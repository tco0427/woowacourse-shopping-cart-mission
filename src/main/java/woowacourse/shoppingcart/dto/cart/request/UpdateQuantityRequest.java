package woowacourse.shoppingcart.dto.cart.request;

public class UpdateQuantityRequest {

    private Long cartItemId;
    private int quantity;

    private UpdateQuantityRequest() {
    }

    public UpdateQuantityRequest(Long cartItemId, int quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }
}

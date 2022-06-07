package woowacourse.shoppingcart.dto.cart.request;

import javax.validation.constraints.Min;

public class UpdateQuantityRequest {

    private Long cartItemId;

    @Min(value = 1, message = "Invalid Quantity")
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

package woowacourse.shoppingcart.dto.cart.request;

import javax.validation.constraints.Min;

public class CartRequest {

    private Long productId;

    @Min(value = 1, message = "Invalid Quantity")
    private int quantity;

    private CartRequest() {
    }

    public CartRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}

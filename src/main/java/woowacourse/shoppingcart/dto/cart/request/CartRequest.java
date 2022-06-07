package woowacourse.shoppingcart.dto.cart.request;

public class CartRequest {

    private Long productId;
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

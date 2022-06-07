package woowacourse.shoppingcart.dto.cart.request;

import java.util.List;

public class CartRemovalRequest {

    List<Long> cartItemIds;

    private CartRemovalRequest() {
    }

    public CartRemovalRequest(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

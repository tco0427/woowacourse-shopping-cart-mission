package woowacourse.shoppingcart.dto.order.request;

import java.util.List;

public class OrderRequest {

    private List<Long> cartItemIds;

    private OrderRequest() {
    }

    public OrderRequest(List<Long> cartItemIds) {
        this.cartItemIds = cartItemIds;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }
}

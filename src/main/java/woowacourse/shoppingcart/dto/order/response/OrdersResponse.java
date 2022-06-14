package woowacourse.shoppingcart.dto.order.response;

import java.util.List;

public class OrdersResponse {

    private List<OrderResponse> orders;

    private OrdersResponse() {
    }

    public OrdersResponse(List<OrderResponse> orders) {
        this.orders = orders;
    }

    public List<OrderResponse> getOrders() {
        return orders;
    }
}

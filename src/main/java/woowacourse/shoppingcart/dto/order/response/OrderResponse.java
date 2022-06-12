package woowacourse.shoppingcart.dto.order.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.dto.product.response.ProductResponse;

public class OrderResponse {

    private Long id;
    private List<ProductResponse> orderedProducts;

    private OrderResponse() {
    }

    public static OrderResponse from(Orders orders) {
        final List<ProductResponse> orderedProducts = orders.getOrderDetails()
                .stream()
                .map(orderDetail -> ProductResponse.from(orderDetail.getProduct()))
                .collect(toList());

        return new OrderResponse(orders.getId(), orderedProducts);
    }

    public OrderResponse(Long id, List<ProductResponse> orderedProducts) {
        this.id = id;
        this.orderedProducts = orderedProducts;
    }

    public Long getId() {
        return id;
    }

    public List<ProductResponse> getOrderedProducts() {
        return orderedProducts;
    }
}

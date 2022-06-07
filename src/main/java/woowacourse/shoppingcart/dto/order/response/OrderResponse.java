package woowacourse.shoppingcart.dto.order.response;

import static java.util.stream.Collectors.toList;

import java.util.List;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.dto.product.response.ProductResponse;

public class OrderResponse {

    private final Long id;
    private final List<ProductResponse> orderedProducts;

    public OrderResponse(Orders orders) {
        this.id = orders.getId();
        this.orderedProducts = orders.getOrderDetails().stream()
                .map(orderDetail -> new ProductResponse(orderDetail.getProduct()))
                .collect(toList());
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

package woowacourse.shoppingcart.domain;

import woowacourse.shoppingcart.domain.vo.OrderQuantity;

public class OrderDetail {

    private final Long id;
    private final Product product;
    private final OrderQuantity orderQuantity;

    public OrderDetail(Product product, int quantity) {
        this(null, product, quantity);
    }

    public OrderDetail(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.orderQuantity = OrderQuantity.from(quantity);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return orderQuantity.getQuantity();
    }
}

package woowacourse.shoppingcart.domain;

public class OrderDetail {

    private final Long id;
    private final Product product;
    private final int quantity;

    public OrderDetail(Product product, int quantity) {
        this(null, product, quantity);
    }

    public OrderDetail(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

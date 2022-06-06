package woowacourse.shoppingcart.domain;

public class Cart {

    private final Long id;
    private final Product product;
    private final int quantity;

    public Cart( Product product, int quantity) {
        this(null, product, quantity);
    }

    public Cart(Long id, Product product, int quantity) {
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

package woowacourse.shoppingcart.domain;

import woowacourse.shoppingcart.domain.vo.ProductQuantity;

public class Cart {

    private final Long id;
    private final Product product;
    private final ProductQuantity productQuantity;

    public Cart( Product product, int quantity) {
        this(null, product, quantity);
    }

    public Cart(Long id, Product product, int quantity) {
        this.id = id;
        this.product = product;
        this.productQuantity = ProductQuantity.from(quantity);
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return productQuantity.getQuantity();
    }
}

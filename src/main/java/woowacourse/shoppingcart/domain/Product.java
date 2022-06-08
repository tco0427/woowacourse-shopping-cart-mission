package woowacourse.shoppingcart.domain;

import woowacourse.shoppingcart.domain.vo.Price;
import woowacourse.shoppingcart.domain.vo.Quantity;

public class Product {

    private final Long id;
    private final String name;
    private final Price price;
    private final Quantity stockQuantity;
    private final Image image;

    public Product(String name, int price, int stockQuantity, Image image) {
        this(null, name, price, stockQuantity, image);
    }

    public Product(Long id, String name, int price, int stockQuantity, Image image) {
        this.id = id;
        this.name = name;
        this.price = Price.from(price);
        this.stockQuantity = Quantity.from(stockQuantity);
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price.getPrice();
    }

    public int getStockQuantity() {
        return stockQuantity.getQuantity();
    }

    public Image getImage() {
        return image;
    }
}

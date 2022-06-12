package woowacourse.shoppingcart.dto.product.response;

import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.ThumbnailImage;

public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private int quantity;
    private ThumbnailImage thumbnailImage;

    private ProductResponse() {
    }

    public ProductResponse(Long id, Product product) {
        this(id, product.getName(), product.getPrice(), product.getStockQuantity(),
                new ThumbnailImage(product.getImage()));
    }

    public static ProductResponse from(Product product) {
        final ThumbnailImage thumbnailImage = new ThumbnailImage(product.getImage());
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStockQuantity(),
                thumbnailImage);
    }

    public ProductResponse(Long id, String name, int price, int quantity, ThumbnailImage thumbnailImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.thumbnailImage = thumbnailImage;
    }
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public ThumbnailImage getThumbnailImage() {
        return thumbnailImage;
    }
}

package woowacourse.shoppingcart.dto.product.request;

import woowacourse.shoppingcart.dto.ThumbnailImage;

public class ProductRequest {

    private String name;
    private int price;
    private int stockQuantity;
    private ThumbnailImage thumbnailImage;

    private ProductRequest() {
    }

    public ProductRequest(String name, int price, int stockQuantity, ThumbnailImage thumbnailImage) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.thumbnailImage = thumbnailImage;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public ThumbnailImage getThumbnailImage() {
        return thumbnailImage;
    }
}

package woowacourse.shoppingcart.dto.product.request;

import javax.validation.constraints.Min;
import woowacourse.shoppingcart.dto.ThumbnailImageDto;

public class ProductRequest {

    private String name;

    @Min(value = 0, message = "Invalid Price")
    private int price;

    @Min(value = 1, message = "Invalid Quantity")
    private int stockQuantity;
    private ThumbnailImageDto thumbnailImageDto;

    private ProductRequest() {
    }

    public ProductRequest(String name, int price, int stockQuantity, ThumbnailImageDto thumbnailImageDto) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.thumbnailImageDto = thumbnailImageDto;
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

    public ThumbnailImageDto getThumbnailImage() {
        return thumbnailImageDto;
    }
}

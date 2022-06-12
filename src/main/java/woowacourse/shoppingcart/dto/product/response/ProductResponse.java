package woowacourse.shoppingcart.dto.product.response;

import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.ThumbnailImageDto;

public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private int quantity;
    private ThumbnailImageDto thumbnailImageDto;

    private ProductResponse() {
    }

    public ProductResponse(Long id, Product product) {
        this(id, product.getName(), product.getPrice(), product.getStockQuantity(),
                new ThumbnailImageDto(product.getImage()));
    }

    public static ProductResponse from(Product product) {
        final ThumbnailImageDto thumbnailImageDto = new ThumbnailImageDto(product.getImage());
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStockQuantity(),
                thumbnailImageDto);
    }

    public ProductResponse(Long id, String name, int price, int quantity, ThumbnailImageDto thumbnailImageDto) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.thumbnailImageDto = thumbnailImageDto;
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

    public ThumbnailImageDto getThumbnailImage() {
        return thumbnailImageDto;
    }
}

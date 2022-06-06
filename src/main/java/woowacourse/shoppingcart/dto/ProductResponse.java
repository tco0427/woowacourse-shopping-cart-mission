package woowacourse.shoppingcart.dto;

import woowacourse.shoppingcart.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private ThumbnailImage thumbnailImage;

    private ProductResponse() {
    }

    public ProductResponse(Long id, Product product) {
        this(id, product.getName(), product.getPrice(), product.getStockQuantity(),
                new ThumbnailImage(product.getImage()));
    }

    public ProductResponse(Product product) {
        this(product.getId(), product.getName(), product.getPrice(),
                product.getStockQuantity(), new ThumbnailImage(product.getImage()));
    }

    public ProductResponse(Long id, String name, int price, int stockQuantity, ThumbnailImage thumbnailImage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
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

    public int getStockQuantity() {
        return stockQuantity;
    }

    public ThumbnailImage getThumbnailImage() {
        return thumbnailImage;
    }
}

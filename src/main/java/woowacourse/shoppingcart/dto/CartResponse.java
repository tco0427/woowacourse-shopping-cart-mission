package woowacourse.shoppingcart.dto;

import woowacourse.shoppingcart.domain.Cart;

public class CartResponse {

    private Long id;
    private Long productId;
    private String name;
    private int price;
    private int quantity;
    private ThumbnailImage thumbnailImage;

    private CartResponse() {
    }

    public CartResponse(Cart cart) {
        this(cart.getId(), cart.getProduct().getId(), cart.getProduct().getName(),
                cart.getProduct().getPrice(), cart.getQuantity(),
                new ThumbnailImage(cart.getProduct().getImage()));
    }

    public CartResponse(Long id, Long productId, String name, int price, int quantity, ThumbnailImage thumbnailImage) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.thumbnailImage = thumbnailImage;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
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

package woowacourse.shoppingcart.dto.cart.response;

import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.ThumbnailImage;

public class CartResponse {

    private Long id;
    private Long productId;
    private String name;
    private int price;
    private int quantity;
    private ThumbnailImage thumbnailImage;

    private CartResponse() {
    }

    public static CartResponse from(Cart cart) {
        final Product product = cart.getProduct();
        final ThumbnailImage thumbnailImage = new ThumbnailImage(cart.getProduct().getImage());

        return new CartResponse(cart.getId(), product.getId(),
                product.getName(), product.getPrice(), cart.getQuantity(), thumbnailImage);
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

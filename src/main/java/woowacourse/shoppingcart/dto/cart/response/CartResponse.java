package woowacourse.shoppingcart.dto.cart.response;

import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.ThumbnailImageDto;

public class CartResponse {

    private Long id;
    private Long productId;
    private String name;
    private int price;
    private int quantity;
    private ThumbnailImageDto thumbnailImage;

    private CartResponse() {
    }

    public static CartResponse from(Cart cart) {
        final Product product = cart.getProduct();
        final ThumbnailImageDto thumbnailImageDto = new ThumbnailImageDto(cart.getProduct().getImage());

        return new CartResponse(cart.getId(), product.getId(),
                product.getName(), product.getPrice(), cart.getQuantity(), thumbnailImageDto);
    }

    public CartResponse(Long id, Long productId, String name, int price, int quantity, ThumbnailImageDto thumbnailImageDto) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.thumbnailImage = thumbnailImageDto;
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

    public ThumbnailImageDto getThumbnailImage() {
        return thumbnailImage;
    }
}

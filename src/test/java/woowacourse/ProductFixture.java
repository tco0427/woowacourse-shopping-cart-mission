package woowacourse;

import woowacourse.shoppingcart.domain.Image;
import woowacourse.shoppingcart.dto.ThumbnailImageDto;
import woowacourse.shoppingcart.dto.product.request.ProductRequest;

public class ProductFixture {

    public static final ThumbnailImageDto CHICKEN_IMAGE =
            new ThumbnailImageDto("http://example.com/chicken.jpg", "chicken");
    public static final ThumbnailImageDto BEER_IMAGE =
            new ThumbnailImageDto("http://example.com/beer.jpg", "beer");
    public static final Image CHOCOLATE_IMAGE = new Image("chocolateImageUrl", "chocolateImageAlt");
    public static final Image SNACK_IMAGE = new Image("snackImageUrl", "snackImageAlt");
    public static final ThumbnailImageDto CHOCOLATE_IMAGE_DTO =
            new ThumbnailImageDto("chocolateImageUrl", "chocolateImageAlt");
    public static final ThumbnailImageDto SNACK_IMAGE_DTO =
            new ThumbnailImageDto("snackImageUrl", "snackImageAlt");
    public static final ThumbnailImageDto SAMPLE_IMAGE_DTO = new ThumbnailImageDto("sampleUrl", "sampleAlt");
    public static final ProductRequest CHOCOLATE_PRODUCT_REQUEST =
            new ProductRequest("chocolate", 1_000, 100, CHOCOLATE_IMAGE_DTO);
    public static final ProductRequest SAMPLE_PRODUCT_REQUEST =
            new ProductRequest("sample", 10_000, 10, SAMPLE_IMAGE_DTO);
    public static final ProductRequest SNACK_PRODUCT_REQUEST =
            new ProductRequest("snack", 1_000, 100, SNACK_IMAGE_DTO);

    private ProductFixture() {
    }
}

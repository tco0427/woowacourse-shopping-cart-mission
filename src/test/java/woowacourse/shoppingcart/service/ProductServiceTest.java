package woowacourse.shoppingcart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import woowacourse.shoppingcart.dto.ThumbnailImageDto;
import woowacourse.shoppingcart.dto.product.request.ProductRequest;
import woowacourse.shoppingcart.dto.product.response.ProductResponse;
import woowacourse.shoppingcart.exception.NotExistException;

@SpringBootTest
class ProductServiceTest {

    private static final ThumbnailImageDto CHOCOLATE_IMAGE =
            new ThumbnailImageDto("chocolateImageUrl", "chocolateImageAlt");
    private static final ThumbnailImageDto SNACK_IMAGE = new ThumbnailImageDto("snackImageUrl", "snackImageAlt");
    private static final ThumbnailImageDto SAMPLE_IMAGE = new ThumbnailImageDto("sampleUrl", "sampleAlt");

    private final ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @DisplayName("상품 정보를 입력하여 상품을 등록할 수 있다.")
    @Test
    public void save() {
        // given
        final ProductRequest request = new ProductRequest("상품", 10_000, 10, SAMPLE_IMAGE);

        // when
        final Long savedId = productService.save(request).getId();

        // then
        final ProductResponse response = productService.findById(savedId);
        assertThat(response.getId()).isEqualTo(savedId);
    }

    @DisplayName("등록된 상품 정보 목록을 조회할 수 있다.")
    @Test
    public void findAll() {
        // given
        final ProductRequest chocolateRequest =
                new ProductRequest("초콜렛", 1_000, 100, CHOCOLATE_IMAGE);
        final Long savedChocolateId = productService.save(chocolateRequest).getId();

        final ProductRequest snackRequest =
                new ProductRequest("과자", 1_500, 1_000, SNACK_IMAGE);
        final Long savedSnackId = productService.save(chocolateRequest).getId();

        // when
        final List<ProductResponse> productResponses = productService.findAll();

        // then
        assertThat(productResponses)
                .hasSize(2)
                .extracting("id")
                .contains(savedChocolateId, savedSnackId);
    }

    @DisplayName("상품 id를 통해 조회해서 해당 상품이 등록되었는지를 확인할 수 있다.")
    @Test
    public void findById() {
        // given
        final ProductRequest request = new ProductRequest("상품", 10_000, 10, SAMPLE_IMAGE);
        final Long savedId = productService.save(request).getId();

        // when
        final ProductResponse response = productService.findById(savedId);

        // then
        assertThat(response.getId()).isEqualTo(savedId);
    }

    @DisplayName("상품 id를 통해서 해당 상품을 제거할 수 있다.")
    @Test
    public void deleteById() {
        // given
        final ProductRequest request = new ProductRequest("상품", 10_000, 10, SAMPLE_IMAGE);
        final Long savedId = productService.save(request).getId();

        // when
        productService.deleteById(savedId);

        // then
        assertThatThrownBy(() -> productService.findById(savedId))
                .isInstanceOf(NotExistException.class);
    }
}

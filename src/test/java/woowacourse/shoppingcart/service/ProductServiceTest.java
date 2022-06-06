package woowacourse.shoppingcart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import woowacourse.shoppingcart.domain.Image;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.exception.NotExistException;

@SpringBootTest
class ProductServiceTest {

    private static final Image CHOCOLATE_IMAGE = new Image("chocolateImageUrl", "chocolateImageAlt");
    private static final Image SNACK_IMAGE = new Image("snackImageUrl", "snackImageAlt");
    private static final Image SAMPLE_IMAGE = new Image("sampleUrl", "sampleAlt");

    private final ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @DisplayName("상품 정보를 입력하여 상품을 등록할 수 있다.")
    @Test
    public void save() {
        // given
        final Product product = new Product("상품", 10_000, 10, SAMPLE_IMAGE);

        // when
        final Long savedId = productService.save(product);

        // then
        final Product foundProduct = productService.findById(savedId);
        assertThat(foundProduct)
                .extracting("name", "price", "stockQuantity", "image")
                .contains(foundProduct.getName(), foundProduct.getPrice(),
                        foundProduct.getStockQuantity(), foundProduct.getImage());
    }

    @DisplayName("등록된 상품 정보 목록을 조회할 수 있다.")
    @Test
    public void findAll() {
        // given
        final Product chocolate = new Product("초콜렛", 1_000, 100, CHOCOLATE_IMAGE);
        productService.save(chocolate);

        final Product snack = new Product("과자", 1_500, 1_000, SNACK_IMAGE);
        productService.save(snack);

        // when
        final List<Product> products = productService.findAll();

        // then
        assertThat(products).hasSize(2)
                .extracting("name", "price", "stockQuantity", "image")
                .contains(
                        tuple(chocolate.getName(), chocolate.getPrice(),
                                chocolate.getStockQuantity(), chocolate.getImage()),
                        tuple(snack.getName(), snack.getPrice(),
                                snack.getStockQuantity(), snack.getImage())
                );
    }

    @DisplayName("상품 id를 통해 조회해서 해당 상품이 등록되었는지를 확인할 수 있다.")
    @Test
    public void findById() {
        // given
        final Product product = new Product("상품", 10_000, 10, SAMPLE_IMAGE);
        final Long savedId = productService.save(product);

        // when
        final Product foundProduct = productService.findById(savedId);

        // then
        assertThat(foundProduct)
                .extracting("name", "price", "stockQuantity", "image")
                .contains(foundProduct.getName(), foundProduct.getPrice(),
                        foundProduct.getStockQuantity(), foundProduct.getImage());
    }

    @DisplayName("상품 id를 통해서 해당 상품을 제거할 수 있다.")
    @Test
    public void deleteById() {
        // given
        final Product product = new Product("상품", 10_000, 10, SAMPLE_IMAGE);
        final Long savedId = productService.save(product);

        // when
        productService.deleteById(savedId);

        // then
        assertThatThrownBy(() -> productService.findById(savedId))
                .isInstanceOf(NotExistException.class);
    }
}

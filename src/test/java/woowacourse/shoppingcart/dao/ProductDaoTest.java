package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static woowacourse.ProductFixture.CHOCOLATE_IMAGE;
import static woowacourse.ProductFixture.SNACK_IMAGE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.Image;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql("classpath:schema.sql")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class ProductDaoTest {
    
    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;

    public ProductDaoTest(JdbcTemplate jdbcTemplate) {
        this.productDao = new ProductDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.customerDao = new CustomerDao(jdbcTemplate);
    }

    @DisplayName("product 정보를 저장하면, id값을 반환해준다.")
    @Test
    void save() {
        // given
        final Product product = new Product("초콜렛", 1_000, 100, CHOCOLATE_IMAGE);

        // when
        final Long savedId = productDao.save(product);

        // then
        final Product foundProduct = productDao.findById(savedId);
        assertThat(foundProduct)
                .extracting("name", "price", "stockQuantity", "image")
                .contains(foundProduct.getName(), foundProduct.getPrice(),
                        foundProduct.getStockQuantity(), foundProduct.getImage());
    }

    @DisplayName("product ID를 통해서 저장된 product를 조회할 수 있다.")
    @Test
    void findProductById() {
        // given
        final Product product = new Product("초콜렛", 1_000, 100, CHOCOLATE_IMAGE);
        final Long savedId = productDao.save(product);

        // when
        final Product foundProduct = productDao.findById(savedId);

        // then
        final Product expectedProduct = new Product(savedId, "초콜렛",
                1_000, 100, CHOCOLATE_IMAGE);
        assertThat(foundProduct).usingRecursiveComparison().isEqualTo(expectedProduct);
    }

    @DisplayName("상품 전체 목록을 조회할 수 있다.")
    @Test
    void getProducts() {
        // given
        final Product chocolate = new Product("초콜렛", 1_000, 100, CHOCOLATE_IMAGE);
        productDao.save(chocolate);

        final Product snack = new Product("과자", 1_500, 1_000, SNACK_IMAGE);
        productDao.save(snack);

        // when
        final List<Product> products = productDao.findAll();

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

    @DisplayName("싱품 삭제")
    @Test
    void deleteProduct() {
        // given
        final Product chocolate = new Product("초콜렛", 1_000, 100, CHOCOLATE_IMAGE);
        final Long savedId = productDao.save(chocolate);
        final int beforeSize = productDao.findAll().size();

        // when & then
        assertDoesNotThrow(() -> productDao.deleteById(savedId));
        final int afterSize = productDao.findAll().size();
        assertThat(beforeSize -1).isEqualTo(afterSize);
    }

    @DisplayName("장바구니 아이템의 id를 통해서 장바구니의 담긴 상품의 재고수량을 수정할 수 있다.")
    @Test
    public void updateStockQuantity() {
        // given
        final Customer customer = new Customer("email@email.com", "password1!", "dwoo");
        final Long customerSavedId = customerDao.save(customer);

        final Long productSavedId = productDao.save(new Product("banana", 1_000, 100,
                new Image("imageUrl", "imageALt")));

        final Long cartSavedId = cartItemDao.addCartItem(customerSavedId, productSavedId, 10);

        // when
        productDao.updateStockQuantity(cartSavedId, productSavedId);

        // then
        final Product product = productDao.findById(productSavedId);
        assertThat(product.getStockQuantity()).isEqualTo(90);
    }
}

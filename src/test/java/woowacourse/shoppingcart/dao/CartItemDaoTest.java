package woowacourse.shoppingcart.dao;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.Image;
import woowacourse.shoppingcart.domain.Product;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CartItemDaoTest {

    private static final Long CUSTOMER_ID = 1L;

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final JdbcTemplate jdbcTemplate;

    public CartItemDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        cartItemDao = new CartItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        productDao.save(new Product("banana", 1_000, 100, new Image("imageUrl", "imageALt")));
        productDao.save(new Product("apple", 2_000, 200, new Image("imageUrl", "imageAlt")));

        jdbcTemplate.update("INSERT INTO cart_item(customer_id, product_id, quantity) VALUES(?, ?, ?)", CUSTOMER_ID, 1L, 100);
        jdbcTemplate.update("INSERT INTO cart_item(customer_id, product_id, quantity) VALUES(?, ?, ?)", CUSTOMER_ID, 2L, 200);
    }

    @DisplayName("카트에 아이템을 담으면, 담긴 카트의 아이디를 반환한다.")
    @Test
    void addCartItem() {
        // given
        final Long productId = 1L;
        final int quantity = 100;

        // when
        final Long cartId = cartItemDao.addCartItem(CUSTOMER_ID, productId, quantity);

        // then
        assertThat(cartId).isEqualTo(3L);
    }

    @DisplayName("고객 정보(id)를 넣으면, 해당 고객이 장바구니에 담은 상품의 id목록을 가져온다.")
    @Test
    void findProductIdsByCustomerId() {
        // given & when
        final List<Cart> carts = cartItemDao.findCartsByCustomerId(CUSTOMER_ID);

        // then
        final List<Long> cartIds = changeCartsToIds(carts);
        assertThat(cartIds).containsExactly(1L, 2L);
    }

    @DisplayName("Customer Id를 넣으면, 해당 장바구니 Id들을 가져온다.")
    @Test
    void findIdsByCustomerId() {
        // given & when
        final List<Cart> carts = cartItemDao.findCartsByCustomerId(CUSTOMER_ID);

        // then
        final List<Long> cartIds = changeCartsToIds(carts);
        assertThat(cartIds).containsExactly(1L, 2L);
    }

    @DisplayName("Customer Id를 넣으면, 해당 장바구니 Id들을 가져온다.")
    @Test
    void deleteCartItem() {
        // given
        final List<Long> cartIds = List.of(1L);

        // when
        cartItemDao.deleteCartItem(cartIds);

        // then
        final List<Cart> carts = cartItemDao.findCartsByCustomerId(CUSTOMER_ID);
        final List<Long> foundCartIds = changeCartsToIds(carts);
        assertThat(foundCartIds).containsExactly(2L);
    }

    private List<Long> changeCartsToIds(List<Cart> carts) {
        return carts.stream()
                .map(Cart::getId)
                .collect(toList());
    }
}

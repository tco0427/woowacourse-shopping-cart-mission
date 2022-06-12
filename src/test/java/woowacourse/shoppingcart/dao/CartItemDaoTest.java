package woowacourse.shoppingcart.dao;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.CustomerFixture.SAMPLE_CUSTOMER;
import static woowacourse.CustomerFixture.CUSTOMER_EMAIL;

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
import woowacourse.shoppingcart.domain.customer.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CartItemDaoTest {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final JdbcTemplate jdbcTemplate;

    public CartItemDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        cartItemDao = new CartItemDao(jdbcTemplate);
        productDao = new ProductDao(jdbcTemplate);
        customerDao = new CustomerDao(jdbcTemplate);
    }

    @BeforeEach
    void setUp() {
        final Long savedId = customerDao.save(SAMPLE_CUSTOMER);

        productDao.save(new Product("banana", 1_000, 100,
                new Image("imageUrl", "imageALt")));
        productDao.save(new Product("apple", 2_000, 200,
                new Image("imageUrl", "imageAlt")));

        jdbcTemplate.update("INSERT INTO cart_item(customer_id, product_id, quantity) VALUES(?, ?, ?)",
                savedId, 1L, 100);
        jdbcTemplate.update("INSERT INTO cart_item(customer_id, product_id, quantity) VALUES(?, ?, ?)",
                savedId, 2L, 200);
    }

    @DisplayName("카트에 아이템을 담으면, 담긴 카트의 아이디를 반환한다.")
    @Test
    void addCartItem() {
        // given
        final Long productId = 1L;
        final int quantity = 100;

        // when
        final Long cartId = cartItemDao.addCartItem(getCustomerId(), productId, quantity);

        // then
        assertThat(cartId).isEqualTo(3L);
    }

    @DisplayName("장바구니에 담긴 cart item의 id로 특정 장바구니에 담긴 상품을 조회한다.")
    @Test
    public void findById() {
        // when
        final Cart foundCart = cartItemDao.findByEmailAndId(CUSTOMER_EMAIL, 1L);

        // then
        assertThat(foundCart)
                .extracting("id", "product", "quantity")
                .contains(foundCart.getId(), foundCart.getProduct(), foundCart.getQuantity());
    }

    @DisplayName("고객 정보(id)를 넣으면, 해당 고객이 장바구니에 담은 상품의 id목록을 가져온다.")
    @Test
    void findProductIdsByCustomerId() {
        // when
        final List<Cart> carts = cartItemDao.findCartsByCustomerId(getCustomerId());

        // then
        final List<Long> cartIds = changeCartsToIds(carts);
        assertThat(cartIds).containsExactly(1L, 2L);
    }

    @DisplayName("Customer Id를 넣으면, 해당 장바구니 Id들을 가져온다.")
    @Test
    void findIdsByCustomerId() {
        // when
        final List<Cart> carts = cartItemDao.findCartsByCustomerId(getCustomerId());

        // then
        final List<Long> cartIds = changeCartsToIds(carts);
        assertThat(cartIds).containsExactly(1L, 2L);
    }

    @DisplayName("장바구니 아이템 id 목록을 통해서 장바구니에서 제거할 수 있다.")
    @Test
    void deleteCartItem() {
        // given
        final List<Long> cartIds = List.of(1L);

        // when
        cartItemDao.deleteCartItem(cartIds);

        // then
        final List<Cart> carts = cartItemDao.findCartsByCustomerId(getCustomerId());
        final List<Long> foundCartIds = changeCartsToIds(carts);
        assertThat(foundCartIds).containsExactly(2L);
    }

    private List<Long> changeCartsToIds(List<Cart> carts) {
        return carts.stream()
                .map(Cart::getId)
                .collect(toList());
    }

    private Long getCustomerId() {
        final Customer customer = customerDao.findByEmail(CUSTOMER_EMAIL);
        return customer.getId();
    }
}

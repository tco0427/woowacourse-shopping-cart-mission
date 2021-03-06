
package woowacourse.shoppingcart.dao;

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
import woowacourse.shoppingcart.domain.Order;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final OrderDao orderDao;
    private final CustomerDao customerDao;
    private final CartItemDao cartItemDao;
    private final ProductDao productDao;

    public OrderDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.orderDao = new OrderDao(jdbcTemplate);
        this.customerDao = new CustomerDao(jdbcTemplate);
        this.cartItemDao = new CartItemDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
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

    @DisplayName("????????? ??? ????????? ??????????????? ?????? ????????? ????????? ??? ??????.")
    @Test
    void addOrders() {
        //given
        final Long cartId = cartItemDao.addCartItem(getCustomerId(), 1L, 10);
        final Cart cart = cartItemDao.findByEmailAndId(CUSTOMER_EMAIL, cartId);
        final Customer customer = customerDao.findByEmail(CUSTOMER_EMAIL);

        //when
        final Long orderId = orderDao.addOrders(customer, List.of(cart));

        //then
        final Order order = orderDao.findById(orderId);
        assertThat(order.getId()).isEqualTo(orderId);
    }

    @DisplayName("?????? id(pk)??? ????????? ?????? ????????? ????????? ??? ??????.")
    @Test
    public void findById() {
        // given
        final Long cartId = cartItemDao.addCartItem(getCustomerId(), 1L, 10);
        final Cart cart = cartItemDao.findByEmailAndId(CUSTOMER_EMAIL, cartId);
        final Customer customer = customerDao.findByEmail(CUSTOMER_EMAIL);
        final Long orderId = orderDao.addOrders(customer, List.of(cart));

        // when
        final Order order = orderDao.findById(orderId);

        // then
        assertThat(order.getId()).isEqualTo(orderId);
        assertThat(order.getOrderDetails().size()).isEqualTo(1);
    }

    @DisplayName("????????? ???????????? ?????? ?????? ????????? ?????? ????????? ??? ??????.")
    @Test
    public void findAll() {
        // given
        final Long cartId1 = cartItemDao.addCartItem(getCustomerId(), 1L, 10);
        final Cart cart1 = cartItemDao.findByEmailAndId(CUSTOMER_EMAIL, cartId1);
        final Long cartId2 = cartItemDao.addCartItem(getCustomerId(), 2L, 5);
        final Cart cart2 = cartItemDao.findByEmailAndId(CUSTOMER_EMAIL, cartId2);

        final Customer customer = customerDao.findByEmail(CUSTOMER_EMAIL);
        final Long orderId1 = orderDao.addOrders(customer, List.of(cart1));
        final Long orderId2 = orderDao.addOrders(customer, List.of(cart2));

        // when
        final List<Order> orders = orderDao.findAll(customer);

        // then
        assertThat(orders.size()).isEqualTo(2);
        assertThat(orders).hasSize(2)
                .extracting("id")
                .contains(orderId1, orderId2);
    }

    private Long getCustomerId() {
        final Customer customer = customerDao.findByEmail(CUSTOMER_EMAIL);
        return customer.getId();
    }
}

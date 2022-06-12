package woowacourse.shoppingcart.dao;

import static java.lang.Boolean.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.Image;
import woowacourse.shoppingcart.domain.Order;
import woowacourse.shoppingcart.domain.OrderDetail;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.exception.NotExistException;

@Repository
public class OrderDao {

    private static final RowMapper<OrderDetail> ORDERS_DETAIL_ROW_MAPPER = createOrdersDetailRowMapper();
    private static final RowMapper<Long> ORDERS_ID_FOW_MAPPER = createOrdersIdRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert ordersJdbcInsert;

    public OrderDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.ordersJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders")
                .usingGeneratedKeyColumns("id");
    }

    public List<Order> findAll(Customer customer) {
        final List<Long> orderIds = findAllOrderId(customer.getEmail());

        final String query = "SELECT orders_detail.id as id, product.id as product_id, product.name as name, "
                + "product.price as price,  product.stock_quantity, image.image_url as url, image.image_alt as alt, "
                + "orders_detail.quantity as quantity "
                + "FROM orders_detail "
                + "JOIN image ON image.id = product.image_id "
                + "JOIN product ON orders_detail.product_id = product.id "
                + "WHERE orders_detail.orders_id = :orderId";

        return makeOrders(query, orderIds);
    }

    private List<Order> makeOrders(String query, List<Long> orderIds) {
        List<Order> orders = new ArrayList<>();
        for (Long orderId : orderIds) {
            final List<OrderDetail> orderDetails = jdbcTemplate.query(query, Map.of("orderId", orderId),
                    ORDERS_DETAIL_ROW_MAPPER);
            final Order order = new Order(orderId, orderDetails);
            orders.add(order);
        }

        return orders;
    }

    private List<Long> findAllOrderId(String email) {
        final String query = "SELECT orders.id as id "
                + "FROM orders "
                + "JOIN customer on customer.id = orders.customer_id "
                + "WHERE customer.email = :email";

        try {
            return jdbcTemplate.query(query, Map.of("email", email), ORDERS_ID_FOW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new NotExistException();
        }
    }

    public Order findById(Long id) {
        checkValidOrder(id);

        final String query = "SELECT orders_detail.id as id, product.id as product_id, product.name as name, "
                + "product.price as price, product.stock_quantity as stock_quantity, "
                + "image.image_url as url, image.image_alt as alt, "
                + "orders_detail.quantity as quantity "
                + "FROM orders_detail "
                + "JOIN image ON image.id = product.image_id "
                + "JOIN product ON orders_detail.product_id = product.id "
                + "WHERE orders_detail.orders_id = :orderId";

        final List<OrderDetail> orderDetails = jdbcTemplate.query(query, Map.of("orderId", id),
                ORDERS_DETAIL_ROW_MAPPER);

        return new Order(id, orderDetails);
    }

    public Long addOrders(Customer customer, List<Cart> carts) {
        final Long ordersId = saveOrdersId(customer);

        final String sql = "INSERT INTO `orders_detail` (orders_id, product_id, quantity)"
                + " VALUES (:ordersId, :productId, :quantity)";

        SqlParameterSource[] parameters = getParameters(carts, ordersId);
        jdbcTemplate.batchUpdate(sql, parameters);

        return ordersId;
    }

    private Long saveOrdersId(Customer customer) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("customer_id", customer.getId());

        return ordersJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    private SqlParameterSource[] getParameters(List<Cart> carts, Long ordersId) {
        return carts.stream()
                .map(cart -> makeParameterSource(cart, ordersId))
                .collect(Collectors.toList()).toArray(SqlParameterSource[]::new);
    }

    private SqlParameterSource makeParameterSource(Cart cart, Long ordersId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("ordersId", ordersId);
        mapSqlParameterSource.addValue("productId", cart.getProduct().getId());
        mapSqlParameterSource.addValue("quantity", cart.getQuantity());

        return mapSqlParameterSource;
    }

    private static RowMapper<OrderDetail> createOrdersDetailRowMapper() {
        return (resultSet, rowNum) -> {
            final long id = resultSet.getLong("id");

            final long productId = resultSet.getInt("product_id");
            final String name = resultSet.getString("name");
            final int price = resultSet.getInt("price");
            final int stockQuantity = resultSet.getInt("stock_quantity");
            final String url = resultSet.getString("url");
            final String alt = resultSet.getString("alt");
            final Image image = new Image(url, alt);
            final Product product = new Product(productId, name, price, stockQuantity, image);

            final int quantity = resultSet.getInt("quantity");

            return new OrderDetail(id, product, quantity);
        };
    }

    private static RowMapper<Long> createOrdersIdRowMapper() {
        return (resultSet, rowNum) -> resultSet.getLong("id");
    }

    private void checkValidOrder(Long id) {
        final String query = "SELECT EXISTS(SELECT * FROM orders WHERE id = :orderId)";

        final Boolean isExist = jdbcTemplate.queryForObject(query, Map.of("orderId", id), Boolean.class);

        if (FALSE.equals(isExist)) {
            throw new NotExistException("Not Exist Order");
        }
    }

    public boolean isValidOrderId(Long customerId, Long orderId) {
        final String query = "SELECT EXISTS(SELECT * FROM orders WHERE customer_id = :customerId AND id = :orderId)";

        return TRUE.equals(jdbcTemplate.queryForObject(query,
                Map.of("customerId", customerId, "orderId", orderId),
                Boolean.class));
    }
}

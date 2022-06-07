package woowacourse.shoppingcart.dao;

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
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.exception.NotExistException;

@Repository
public class CartItemDao {

    private static final RowMapper<Cart> CART_ITEM_ROW_MAPPER = createCartItemRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CartItemDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_item")
                .usingGeneratedKeyColumns("id");
    }

    public Cart findByEmailAndId(String email, Long id) {
        final String query = "SELECT cart_item.id as id, product.id as product_id, "
                + "product.name as name, product.price as price, product.stock_quantity, "
                + "image.image_url as url, image.image_alt as alt, cart_item.quantity as quantity "
                + "FROM cart_item "
                + "JOIN product ON cart_item.product_id = product.id "
                + "JOIN image ON image.id = product.image_id "
                + "JOIN customer ON customer.id = cart_item.customer_id "
                + "WHERE cart_item.id = :id AND customer.email = :email";

        try {
            return jdbcTemplate.queryForObject(query, Map.of("id", id, "email", email), CART_ITEM_ROW_MAPPER);
        } catch(EmptyResultDataAccessException e) {
            throw new NotExistException("Not Exist CartItem");
        }
    }

    public List<Cart> findByEmailAndIds(String email, List<Long> ids) {
        final String query = "SELECT cart_item.id as id, product.id as product_id, "
                + "product.name as name, product.price as price, product.stock_quantity, "
                + "image.image_url as url, image.image_alt as alt, cart_item.quantity as quantity "
                + "FROM cart_item "
                + "JOIN product ON cart_item.product_id = product.id "
                + "JOIN image ON image.id = product.image_id "
                + "JOIN customer ON customer.id = cart_item.customer_id "
                + "WHERE cart_item.id IN (:ids) AND customer.email = :email";

        try {
            return jdbcTemplate.query(query, Map.of("ids", ids, "email", email), CART_ITEM_ROW_MAPPER);
        } catch(EmptyResultDataAccessException e) {
            throw new NotExistException("Not Exist CartItem");
        }
    }

    public List<Cart> findCartsByCustomerId(Long id) {
        final String query = "SELECT cart_item.id as id, product.id as product_id, "
                + "product.name as name, product.price as price, product.stock_quantity, "
                + "image.image_url as url, image.image_alt as alt, cart_item.quantity as quantity "
                + "FROM cart_item "
                + "JOIN product ON cart_item.product_id = product.id "
                + "JOIN image ON image.id = product.image_id "
                + "WHERE customer_id = :id";

        return jdbcTemplate.query(query, Map.of("id", id), CART_ITEM_ROW_MAPPER);
    }

    public Long addCartItem(Long customerId, Long productId, int quantity) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("customer_id", customerId);
        parameterSource.addValue("product_id", productId);
        parameterSource.addValue("quantity", quantity);

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public void updateCartQuantity(Long cartItemId, int quantity) {
        final String query = "UPDATE cart_item SET quantity = :quantity WHERE id = :id";

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("quantity", quantity);
        parameterSource.addValue("id", cartItemId);

        jdbcTemplate.update(query, parameterSource);
    }

    public void deleteCartItem(List<Long> cartItemIds) {
        final String sql = "DELETE FROM cart_item WHERE id = :id";

        SqlParameterSource[] parameters = getParameters(cartItemIds);
        jdbcTemplate.batchUpdate(sql, parameters);
    }

    private SqlParameterSource[] getParameters(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(this::makeParameterSource)
                .collect(Collectors.toList()).toArray(SqlParameterSource[]::new);
    }

    private SqlParameterSource makeParameterSource(Long cartItemId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", cartItemId);
        return mapSqlParameterSource;
    }

    private static RowMapper<Cart> createCartItemRowMapper() {
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

            return new Cart(id, product, quantity);
        };
    }
}

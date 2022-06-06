package woowacourse.shoppingcart.dao;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.OrderDetail;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.Image;

@Repository
public class OrdersDetailDao {

    private static final RowMapper<OrderDetail> ORDER_DETAIL_ROW_MAPPER = createOrderDetailRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrdersDetailDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("orders_detail")
                .usingGeneratedKeyColumns("id");
    }

    public Long addOrdersDetail(final Long ordersId, final Long productId, final int quantity) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("orders_id", ordersId);
        parameterSource.addValue("product_id", productId);
        parameterSource.addValue("quantity", quantity);

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public List<OrderDetail> findOrdersDetailsByOrderId(final Long orderId) {
        final String query = "SELECT orders_detail.id, product.id as product_id, product.name as product_name, "
                + "product.price as product_price, product.stock_quantity as stockQuantity, "
                + "image.image_url as image_url, image.image_alt as image_alt, "
                + "orders_detail.quantity as quantity FROM orders_detail "
                + "JOIN product on product.id = orders_detail.product_id "
                + "JOIN image on product.image_id = image.id";

        return jdbcTemplate.query(query, Map.of("orderId", orderId), ORDER_DETAIL_ROW_MAPPER);
    }

    private static RowMapper<OrderDetail> createOrderDetailRowMapper() {
        return (resultSet, rowNum) -> {
            final long orderDetailId = resultSet.getLong("id");

            final long productId = resultSet.getLong("product_id");
            final String productName = resultSet.getString("product_name");
            final int productPrice = resultSet.getInt("product_price");
            final int stockQuantity = resultSet.getInt("stockQuantity");
            final String imageUrl = resultSet.getString("image_url");
            final String imageAlt = resultSet.getString("image_alt");
            final Image image = new Image(imageUrl, imageAlt);
            final Product product = new Product(productId, productName, productPrice, stockQuantity, image);

            final int quantity = resultSet.getInt("quantity");

            return new OrderDetail(orderDetailId, product, quantity);
        };
    }
}

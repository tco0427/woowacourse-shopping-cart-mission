package woowacourse.shoppingcart.dao;

import java.util.List;
import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.Image;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.exception.NotExistException;

@Repository
public class ProductDao {

    private static final RowMapper<Product> PRODUCT_ROW_MAPPER = createProductRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert productInsert;
    private final SimpleJdbcInsert imageInsert;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.productInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
        this.imageInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("image")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Product product) {
        Long imageId = saveImage(product.getImage());

        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("name", product.getName());
        parameterSource.addValue("price", product.getPrice());
        parameterSource.addValue("stock_quantity", product.getStockQuantity());
        parameterSource.addValue("image_id", imageId);

        return productInsert.executeAndReturnKey(parameterSource).longValue();
    }

    private Long saveImage(Image image) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("image_url", image.getUrl());
        parameterSource.addValue("image_alt", image.getAlteration());

        return imageInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public Product findById(Long id) {
        final String query = "SELECT product.id, `name`, price, stock_quantity, "
                + "image.image_url as url, image.image_alt as alt "
                + "FROM product "
                + "JOIN image on image.id = image_id "
                + "WHERE product.id = :id";

        try {
            return jdbcTemplate.queryForObject(query, Map.of("id", id), PRODUCT_ROW_MAPPER);
        } catch(EmptyResultDataAccessException e) {
            throw new NotExistException("Not Exist Product");
        }
    }

    public List<Product> findAll() {
        final String query = "SELECT product.id, `name`, price, stock_quantity, "
                + "image.image_url as url, image.image_alt as alt "
                + "FROM product "
                + "JOIN image on image.id = image_id";

        return jdbcTemplate.query(query, PRODUCT_ROW_MAPPER);
    }

    public void deleteById(Long id) {
        final String query = "DELETE FROM product WHERE id = :id";

        jdbcTemplate.update(query, Map.of("id", id));
    }

    public void updateQuantity(Long id, int quantity) {
        final String query = "UPDATE product SET stock_quantity = :quantity WHERE id = :id";

        jdbcTemplate.update(query, Map.of("id", id, "quantity", quantity));
    }

    public void updateStockQuantity(Long cartId, Long productId) {
        final String query = "UPDATE product SET product.stock_quantity = "
                + "(SELECT (product.stock_quantity - cart_item.quantity) "
                + "FROM product "
                + "JOIN cart_item ON cart_item.product_id = product.id "
                + "WHERE cart_item.id = :cartId) "
                + "WHERE id = :productId";

        jdbcTemplate.update(query, Map.of("cartId", cartId, "productId", productId));
    }

    private static RowMapper<Product> createProductRowMapper() {
        return (resultSet, rowNum) -> {
            final long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            final int price = resultSet.getInt("price");
            final int stockQuantity = resultSet.getInt("stock_quantity");

            final String url = resultSet.getString("url");
            final String alt = resultSet.getString("alt");
            final Image image = new Image(url, alt);

            return new Product(id, name, price, stockQuantity, image);
        };
    }
}

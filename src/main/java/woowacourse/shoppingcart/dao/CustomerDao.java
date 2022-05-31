package woowacourse.shoppingcart.dao;

import static java.util.Locale.ROOT;

import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.exception.InvalidCustomerException;

@Repository
public class CustomerDao {

    private static final RowMapper<Customer> CUSTOMER_ROW_MAPPER = (resultSet, rowNum) -> new Customer(
            resultSet.getLong("id"),
            resultSet.getString("email"),
            resultSet.getString("password"),
            resultSet.getString("username")
    );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CustomerDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("customer")
                .usingGeneratedKeyColumns("id");
    }

    public Customer findById(final Long id) {
        final String query = "SELECT * FROM customer WHERE id = :id";

        return jdbcTemplate.queryForObject(query, Map.of("id", id), CUSTOMER_ROW_MAPPER);
    }

    public Customer findByEmail(final String email) {
        final String query = "SELECT * FROM customer WHERE email = :email";

        return jdbcTemplate.queryForObject(query, Map.of("email", email), CUSTOMER_ROW_MAPPER);
    }

    public Long findIdByUserName(final String userName) {
        try {
            final String query = "SELECT id FROM customer WHERE username = :username";
            return jdbcTemplate.queryForObject(query, Map.of("username", userName.toLowerCase(ROOT)), Long.class);
        } catch (final EmptyResultDataAccessException e) {
            throw new InvalidCustomerException();
        }
    }

    public Long save(final Customer customer) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("email", customer.getEmail());
        parameterSource.addValue("password", customer.getPassword());
        parameterSource.addValue("username", customer.getUsername());

        return simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
    }

    public void update(final Customer customer) {
        final String query = "UPDATE customer SET username = :username, password = :password where id = :id";

        final MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("username", customer.getUsername());
        parameterSource.addValue("password", customer.getPassword());
        parameterSource.addValue("id", customer.getId());

        jdbcTemplate.update(query, parameterSource);
    }

    public int deleteById(final Long id) {
        final String query = "DELETE FROM customer WHERE id = :id";
        return jdbcTemplate.update(query, Map.of("id", id));
    }
}

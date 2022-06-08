package woowacourse.shoppingcart.dao;

import java.util.Map;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.exception.NotExistException;

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

        try {
            return jdbcTemplate.queryForObject(query, Map.of("id", id), CUSTOMER_ROW_MAPPER);
        } catch(EmptyResultDataAccessException e) {
            throw new NotExistException("Not Exist Customer");
        }
    }

    public Customer findByEmail(final String email) {
        final String query = "SELECT * FROM customer WHERE email = :email";

        try {
            return jdbcTemplate.queryForObject(query, Map.of("email", email), CUSTOMER_ROW_MAPPER);
        } catch(EmptyResultDataAccessException e) {
            throw new NotExistException();
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

    public void deleteById(final Long id) {
        final String query = "DELETE FROM customer WHERE id = :id";
        jdbcTemplate.update(query, Map.of("id", id));
    }
}

package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static woowacourse.CustomerFixture.SAMPLE_CUSTOMER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.customer.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql(scripts = {"classpath:schema.sql", "classpath:data.sql"})
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CustomerDaoTest {

    private final CustomerDao customerDao;

    public CustomerDaoTest(JdbcTemplate jdbcTemplate) {
        customerDao = new CustomerDao(jdbcTemplate);
    }

    @DisplayName("id 값을 통해서 Customer 를 조회할 수 있다.")
    @Test
    public void findById() {
        // given
        final Customer customer = SAMPLE_CUSTOMER;
        final Long savedId = customerDao.save(customer);

        // when
        final Customer foundCustomer = customerDao.findById(savedId);

        // then
        assertThat(foundCustomer)
                .extracting("email", "password", "username")
                .contains(customer.getEmail(), customer.getPassword(), customer.getUsername());
    }

    @DisplayName("email 은 유일하므로 email 값을 통해서 Customer 를 조회할 수 있다.")
    @Test
    public void findByEmail() {
        // given
        final Customer customer = SAMPLE_CUSTOMER;
        customerDao.save(customer);

        // when
        final Customer foundCustomer = customerDao.findByEmail(customer.getEmail());

        // then
        assertThat(foundCustomer)
                .extracting("email", "password", "username")
                .contains(customer.getEmail(), customer.getPassword(), customer.getUsername());
    }

    @DisplayName("Customer 정보를 저장하고, id를 반환한다.")
    @Test
    public void saveCustomer() {
        // given
        final Customer customer = SAMPLE_CUSTOMER;

        // when
        final Long savedId = customerDao.save(customer);

        // then
        final Customer foundCustomer = customerDao.findById(savedId);
        assertThat(foundCustomer)
                .extracting("email", "password", "username")
                .contains(customer.getEmail(), customer.getPassword(), customer.getUsername());
    }

    @DisplayName("id값을 통해서 비밀번호와 유저 이름을 변경할 수 있다.")
    @Test
    public void update() {
        // given
        final Customer customer = SAMPLE_CUSTOMER;
        final Long savedId = customerDao.save(customer);

        // when
        final Customer changeCustomer = new Customer(savedId,
                "email@email.com", "changepwd1!", "changeName");
        customerDao.update(changeCustomer);

        // then
        final Customer foundCustomer = customerDao.findById(savedId);
        assertThat(foundCustomer)
                .extracting("email", "password", "username")
                .contains(changeCustomer.getEmail(), changeCustomer.getPassword(), changeCustomer.getUsername());
    }

    @DisplayName("id값을 통해서 회원정보를 삭제할 수 있다.")
    @Test
    public void deleteById() {
        // given
        final Long savedId = customerDao.save(SAMPLE_CUSTOMER);

        // when & then
        assertDoesNotThrow(() -> customerDao.deleteById(savedId));
    }
}

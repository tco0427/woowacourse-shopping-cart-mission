package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("username을 통해 아이디를 찾으면, id를 반환한다.")
    @Test
    void findIdByUserNameTest() {

        // given
        final String userName = "puterism";

        // when
        final Long customerId = customerDao.findIdByUserName(userName);

        // then
        assertThat(customerId).isEqualTo(1L);
    }

    @DisplayName("대소문자를 구별하지 않고 username을 통해 아이디를 찾으면, id를 반환한다.")
    @Test
    void findIdByUserNameTestIgnoreUpperLowerCase() {

        // given
        final String userName = "gwangyeol-iM";

        // when
        final Long customerId = customerDao.findIdByUserName(userName);

        // then
        assertThat(customerId).isEqualTo(16L);
    }

    @DisplayName("id 값을 통해서 Customer 를 조회할 수 있다.")
    @Test
    public void findById() {
        // given
        Customer customer = new Customer("email@email.com","password1!","azpi");
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
        Customer customer = new Customer("email@email.com", "password1!", "azpi");
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
        Customer customer = new Customer("email@email.com", "password1!", "azpi");

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
        Customer customer = new Customer("email@email.com", "password1!", "azpi");
        final Long savedId = customerDao.save(customer);

        // when
        final Customer changeCustomer = new Customer(savedId,
                "email@email.com", "changepwd1!", "dwoo");
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
        Customer customer = new Customer("email@email.com", "password1!", "azpi");
        final Long savedId = customerDao.save(customer);

        // when & then
        assertThat(customerDao.deleteById(savedId)).isEqualTo(1);
    }
}

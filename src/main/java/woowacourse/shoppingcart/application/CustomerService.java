package woowacourse.shoppingcart.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.Customer;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.CustomerResponse;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void save(CustomerRequest customerRequest) {
        final Customer customer = new Customer(customerRequest.getEmail(), customerRequest.getPassword(),
                customerRequest.getUsername());

        customerDao.save(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse findByEmail(String email) {
        final Customer customer = customerDao.findByEmail(email);

        return new CustomerResponse(customer.getEmail(), customer.getUsername());
    }
}

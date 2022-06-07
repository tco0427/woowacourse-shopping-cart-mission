package woowacourse.shoppingcart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.customer.request.ChangePasswordRequest;
import woowacourse.shoppingcart.dto.customer.request.CustomerRequest;
import woowacourse.shoppingcart.dto.customer.response.CustomerResponse;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void save(CustomerRequest customerRequest) {
        final Customer customer = customerFromRequest(customerRequest);

        customerDao.save(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse findByEmail(String email) {
        final Customer customer = customerDao.findByEmail(email);

        return new CustomerResponse(customer.getEmail(), customer.getUsername());
    }

    public void changePassword(String email, ChangePasswordRequest request) {
        final Customer foundCustomer = customerDao.findByEmail(email);
        foundCustomer.checkCorrectPassword(request.getOldPassword());

        final Customer customer = new Customer(foundCustomer.getId(), email, request.getNewPassword(),
                foundCustomer.getUsername());
        customerDao.update(customer);
    }

    public CustomerResponse update(String email, String username) {
        updateCustomer(email, username);

        return findByEmail(email);
    }

    private void updateCustomer(String email, String username) {
        final Customer customer = customerDao.findByEmail(email);

        final Customer updateCustomer = new Customer(customer.getId(), email, customer.getPassword(), username);
        customerDao.update(updateCustomer);
    }

    public void delete(String email, String password) {
        final Customer customer = customerDao.findByEmail(email);
        customer.checkCorrectPassword(password);

        customerDao.deleteById(customer.getId());
    }

    private Customer customerFromRequest(CustomerRequest request) {
        return new Customer(request.getEmail(), request.getPassword(), request.getUsername());
    }
}

package woowacourse.shoppingcart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.ChangePasswordRequest;
import woowacourse.shoppingcart.dto.CustomerRequest;
import woowacourse.shoppingcart.dto.CustomerResponse;
import woowacourse.shoppingcart.exception.DeleteException;

@Service
@Transactional(rollbackFor = Exception.class)
public class CustomerService {

    private static final int DELETE_FAIL = 0;

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

    public void changePassword(String email, ChangePasswordRequest request) {
        final Customer foundCustomer = customerDao.findByEmail(email);
        foundCustomer.checkPassword(request.getOldPassword());

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
        customer.checkPassword(password);

        final int deleteCount = customerDao.deleteById(customer.getId());

        if (deleteCount == DELETE_FAIL) {
           throw new DeleteException("고객 정보 삭제에 실패하였습니다.");
        }
    }
}

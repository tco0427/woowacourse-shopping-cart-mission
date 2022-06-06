package woowacourse.shoppingcart.domain;

import java.util.List;
import woowacourse.shoppingcart.domain.customer.Customer;

public class Carts {

    private final Customer customer;
    private final List<Cart> carts;

    public Carts(Customer customer, List<Cart> carts) {
        this.customer = customer;
        this.carts = carts;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Cart> getCarts() {
        return List.copyOf(carts);
    }
}

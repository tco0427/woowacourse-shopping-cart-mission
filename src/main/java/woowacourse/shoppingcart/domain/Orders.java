package woowacourse.shoppingcart.domain;

import java.util.List;
import woowacourse.shoppingcart.domain.customer.Customer;

public class Orders {

    private final Long id;
    private final Customer customer;
    private final List<OrderDetail> orderDetails;

    public Orders(Long id, Customer customer, List<OrderDetail> orderDetails) {
        this.id = id;
        this.customer = customer;
        this.orderDetails = orderDetails;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderDetail> getOrderDetails() {
        return List.copyOf(orderDetails);
    }
}

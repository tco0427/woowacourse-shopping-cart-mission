package woowacourse.shoppingcart.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.auth.exception.InvalidTokenException;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.dao.OrderDao;
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.order.request.OrderRequest;
import woowacourse.shoppingcart.dto.order.response.OrderResponse;
import woowacourse.shoppingcart.dto.order.response.OrdersResponse;
import woowacourse.shoppingcart.exception.InvalidOrderException;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {

    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;

    public OrderService(OrderDao orderDao, CartItemDao cartItemDao, CustomerDao customerDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
    }

    public Long addOrder(String email, OrderRequest request) {
        final Customer customer = customerDao.findByEmail(email);
        final List<Cart> carts = cartItemDao.findByEmailAndIds(customer.getEmail(), request.getCartItemIds());

        checkOverQuantity(carts);

        final List<Long> cartItemIds = convertCartsToCartItemIds(carts);
        cartItemDao.deleteCartItem(cartItemIds);

        return orderDao.addOrders(customer, carts);
    }

    private void checkOverQuantity(List<Cart> carts) {
        final boolean isOverQuantity = carts.stream()
                .anyMatch(cart -> cart.getQuantity() > cart.getProduct().getStockQuantity());

        if (isOverQuantity) {
            throw new InvalidOrderException("Out Of Stock");
        }
    }

    private List<Long> convertCartsToCartItemIds(List<Cart> carts) {
        return carts.stream()
                .map(Cart::getId)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrderById(String email, final Long orderId) {
        validateOrderIdByCustomer(email, orderId);
        final Orders orders = orderDao.findById(orderId);

        return new OrderResponse(orders);
    }

    @Transactional(readOnly = true)
    public OrdersResponse findOrdersByCustomer(String email) {
        final Customer customer = customerDao.findByEmail(email);
        final List<Orders> orders = orderDao.findAll(customer);

        validateOrderIdsByCustomer(email, orders);

        final List<OrderResponse> orderResponses = createOrderResponses(orders);

        return new OrdersResponse(orderResponses);
    }

    private List<OrderResponse> createOrderResponses(List<Orders> orders) {
        return orders.stream()
                .map(OrderResponse::new)
                .collect(toList());
    }

    private void validateOrderIdByCustomer(String email, Long orderId) {
        final Customer customer = customerDao.findByEmail(email);

        if (!orderDao.isValidOrderId(customer.getId(), orderId)) {
            throw new InvalidTokenException("Invalid Token");
        }
    }

    private void validateOrderIdsByCustomer(String email, List<Orders> orders) {
        final List<Long> ordersIds = orders.stream()
                .map(Orders::getId)
                .collect(toList());

        for (Long ordersId : ordersIds) {
            validateOrderIdByCustomer(email, ordersId);
        }
    }
}

package woowacourse.shoppingcart.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.auth.exception.InvalidTokenException;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.dao.OrderDao;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.Order;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.order.request.OrderRequest;
import woowacourse.shoppingcart.dto.order.response.OrderResponse;
import woowacourse.shoppingcart.exception.InvalidOrderException;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {

    private final OrderDao orderDao;
    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    public OrderService(OrderDao orderDao, CartItemDao cartItemDao, CustomerDao customerDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    public Long addOrder(String email, OrderRequest request) {
        final Customer customer = customerDao.findByEmail(email);
        final List<Cart> carts = cartItemDao.findByEmailAndIds(customer.getEmail(), request.getCartItemIds());

        checkOverQuantity(carts);
        reduceStockQuantity(carts);

        final List<Long> cartItemIds = convertCartsToCartItemIds(carts);
        cartItemDao.deleteCartItem(cartItemIds);

        return orderDao.addOrders(customer, carts);
    }

    private void reduceStockQuantity(List<Cart> carts) {
        for (Cart cart : carts) {
            final Product product = productDao.findById(cart.getProduct().getId());
            final int quantity = product.getStockQuantity() - cart.getQuantity();
            productDao.updateQuantity(product.getId(), quantity);
        }
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
        final Order order = orderDao.findById(orderId);

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findOrdersByCustomer(String email) {
        final Customer customer = customerDao.findByEmail(email);
        final List<Order> orders = orderDao.findAll(customer);

        validateOrderIdsByCustomer(email, orders);

        return createOrderResponses(orders);
    }

    private List<OrderResponse> createOrderResponses(List<Order> orders) {
        return orders.stream()
                .map(OrderResponse::from)
                .collect(toList());
    }

    private void validateOrderIdByCustomer(String email, Long orderId) {
        final Customer customer = customerDao.findByEmail(email);

        if (!orderDao.isValidOrderId(customer.getId(), orderId)) {
            throw new InvalidTokenException("Invalid Token");
        }
    }

    private void validateOrderIdsByCustomer(String email, List<Order> orders) {
        final List<Long> ordersIds = orders.stream()
                .map(Order::getId)
                .collect(toList());

        for (Long ordersId : ordersIds) {
            validateOrderIdByCustomer(email, ordersId);
        }
    }
}

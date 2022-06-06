package woowacourse.shoppingcart.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.exception.InvalidCartItemException;

@Service
@Transactional(rollbackFor = Exception.class)
public class CartService {

    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;

    public CartService(final CartItemDao cartItemDao, final CustomerDao customerDao) {
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
    }

    @Transactional(readOnly = true)
    public Cart findById(Long id) {
        return cartItemDao.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Cart> findCartsByCustomer(String email) {
        final Customer customer = customerDao.findByEmail(email);
        return cartItemDao.findCartsByCustomerId(customer.getId());
    }

    public Long addCart(String email, Long productId, int quantity) {
        final Customer customer = customerDao.findByEmail(email);
        return cartItemDao.addCartItem(customer.getId(), productId, quantity);
    }

    public void updateQuantity(String email, Long cartItemId, int quantity) {
        checkCartOwner(email, cartItemId);
        cartItemDao.updateCartQuantity(cartItemId, quantity);
    }

    private void checkCartOwner(String email, Long cartItemId) {
        final List<Cart> carts = findCartsByCustomer(email);
        final List<Long> cartIds = convertCartsToCartIds(carts);

        if (!cartIds.contains(cartItemId)) {
            throw new InvalidCartItemException("Invalid CartItem");
        }
    }

    public void deleteCart(String email, List<Long> cartIds) {
        checkCartOwner(email, cartIds);
        cartItemDao.deleteCartItem(cartIds);
    }

    private void checkCartOwner(String email, List<Long> cartIds) {
        final List<Cart> carts = findCartsByCustomer(email);
        final List<Long> customerCartIds = convertCartsToCartIds(carts);

        if (!customerCartIds.containsAll(cartIds)) {
            throw new InvalidCartItemException("Invalid CartItem");
        }
    }

    private List<Long> convertCartsToCartIds(List<Cart> carts) {
        return carts.stream().map(Cart::getId).collect(toList());
    }
}

package woowacourse.shoppingcart.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.cart.request.CartRemovalRequest;
import woowacourse.shoppingcart.dto.cart.request.CartRequest;
import woowacourse.shoppingcart.dto.cart.request.UpdateQuantityRequest;
import woowacourse.shoppingcart.dto.cart.response.CartResponse;
import woowacourse.shoppingcart.dto.cart.response.CartsResponse;
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
    public CartResponse findById(String email, Long id) {
        final Cart cart = cartItemDao.findByEmailAndId(email, id);

        return new CartResponse(cart);
    }

    @Transactional(readOnly = true)
    public CartsResponse findCartsByCustomer(String email) {
        final Customer customer = customerDao.findByEmail(email);
        final List<Cart> carts = cartItemDao.findCartsByCustomerId(customer.getId());

        final List<CartResponse> cartResponses = carts.stream()
                .map(CartResponse::new)
                .collect(toList());

        return new CartsResponse(cartResponses);
    }

    public CartResponse addCart(String email, CartRequest request) {
        final Customer customer = customerDao.findByEmail(email);
        final Long cartId = cartItemDao.addCartItem(customer.getId(), request.getProductId(), request.getQuantity());
        final Cart cart = cartItemDao.findByEmailAndId(email, cartId);

        return new CartResponse(cart);
    }

    public void updateQuantity(String email, UpdateQuantityRequest request) {
        checkCartOwner(email, request.getCartItemId());
        cartItemDao.updateCartQuantity(request.getCartItemId(), request.getQuantity());
    }

    private void checkCartOwner(String email, Long cartItemId) {
        final List<Cart> carts = findCartsByEmail(email);
        final List<Long> cartIds = convertCartsToCartIds(carts);

        if (!cartIds.contains(cartItemId)) {
            throw new InvalidCartItemException("Invalid CartItem");
        }
    }

    public void deleteCart(String email, CartRemovalRequest request) {
        final List<Long> cartIds = request.getCartItemIds();
        checkCartOwner(email, cartIds);
        cartItemDao.deleteCartItem(cartIds);
    }

    private void checkCartOwner(String email, List<Long> cartIds) {
        final List<Cart> carts = findCartsByEmail(email);
        final List<Long> customerCartIds = convertCartsToCartIds(carts);

        if (!customerCartIds.containsAll(cartIds)) {
            throw new InvalidCartItemException("Invalid CartItem");
        }
    }

    private List<Cart> findCartsByEmail(String email) {
        final Customer customer = customerDao.findByEmail(email);
        return cartItemDao.findCartsByCustomerId(customer.getId());
    }

    private List<Long> convertCartsToCartIds(List<Cart> carts) {
        return carts.stream().map(Cart::getId).collect(toList());
    }
}

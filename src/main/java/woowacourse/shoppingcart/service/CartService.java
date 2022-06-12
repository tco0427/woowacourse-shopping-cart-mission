package woowacourse.shoppingcart.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.Cart;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.cart.request.CartRemovalRequest;
import woowacourse.shoppingcart.dto.cart.request.CartRequest;
import woowacourse.shoppingcart.dto.cart.request.UpdateQuantityRequest;
import woowacourse.shoppingcart.dto.cart.response.CartResponse;
import woowacourse.shoppingcart.exception.InvalidCartItemException;

@Service
@Transactional(rollbackFor = Exception.class)
public class CartService {

    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    public CartService(CartItemDao cartItemDao, CustomerDao customerDao, ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public CartResponse findById(String email, Long id) {
        final Cart cart = cartItemDao.findByEmailAndId(email, id);

        return CartResponse.from(cart);
    }

    @Transactional(readOnly = true)
    public List<CartResponse> findCartsByCustomer(String email) {
        final Customer customer = customerDao.findByEmail(email);
        final List<Cart> carts = cartItemDao.findCartsByCustomerId(customer.getId());

        return carts.stream()
                .map(CartResponse::from)
                .collect(toList());
    }

    public CartResponse addCart(String email, CartRequest request) {
        final Customer customer = customerDao.findByEmail(email);
        checkAlreadyExistCartItem(customer, request);
        checkExistProduct(request.getProductId());

        final Long cartId = cartItemDao.addCartItem(customer.getId(), request.getProductId(), request.getQuantity());
        final Cart cart = cartItemDao.findByEmailAndId(email, cartId);

        return CartResponse.from(cart);
    }

    private void checkAlreadyExistCartItem(Customer customer, CartRequest request) {
        final List<Cart> customerCarts = cartItemDao.findCartsByCustomerId(customer.getId());
        final boolean isExist = customerCarts.stream()
                .anyMatch(cart -> cart.getProduct().getId().equals(request.getProductId()));

        if (isExist) {
            throw new InvalidCartItemException("Already Exist");
        }
    }

    private void checkExistProduct(Long productId) {
        productDao.findById(productId);
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
        return carts.stream()
                .map(Cart::getId)
                .collect(toList());
    }
}

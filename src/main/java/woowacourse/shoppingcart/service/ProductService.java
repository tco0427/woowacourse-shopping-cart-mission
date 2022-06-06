package woowacourse.shoppingcart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.Product;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Long save(final Product product) {
        return productDao.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(final Long productId) {
        return productDao.findById(productId);
    }

    public void deleteById(final Long productId) {
        productDao.deleteById(productId);
    }
}

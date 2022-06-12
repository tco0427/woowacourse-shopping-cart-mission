package woowacourse.shoppingcart.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.Image;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.ThumbnailImageDto;
import woowacourse.shoppingcart.dto.product.request.ProductRequest;
import woowacourse.shoppingcart.dto.product.response.ProductResponse;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductService {
    private final ProductDao productDao;

    public ProductService(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        final List<Product> products = productDao.findAll();

        return products.stream()
                .map(ProductResponse::from)
                .collect(toList());
    }

    public ProductResponse save(ProductRequest request) {
        Image image = makeImage(request.getThumbnailImage());
        final Product product = new Product(request.getName(), request.getPrice(), request.getStockQuantity(), image);

        final Long savedId = productDao.save(product);

        return new ProductResponse(savedId, product);
    }

    private Image makeImage(ThumbnailImageDto thumbnailImageDto) {
        return new Image(thumbnailImageDto.getUrl(), thumbnailImageDto.getAlt());
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(final Long productId) {
        final Product product = productDao.findById(productId);

        return ProductResponse.from(product);
    }

    public void deleteById(final Long productId) {
        productDao.deleteById(productId);
    }
}

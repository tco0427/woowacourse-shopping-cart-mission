package woowacourse.shoppingcart.dto.product.response;

import java.util.List;

public class ProductsResponse {

    private final List<ProductResponse> productResponses;

    public ProductsResponse(List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }
}

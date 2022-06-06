package woowacourse.shoppingcart.dto;

import java.util.List;

public class ProductResponses {

    private List<ProductResponse> productResponses;

    private ProductResponses() {
    }

    public ProductResponses(List<ProductResponse> productResponses) {
        this.productResponses = productResponses;
    }

    public List<ProductResponse> getProductResponses() {
        return productResponses;
    }
}

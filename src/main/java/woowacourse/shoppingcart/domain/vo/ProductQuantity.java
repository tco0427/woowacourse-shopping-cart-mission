package woowacourse.shoppingcart.domain.vo;

import java.util.Objects;

public class ProductQuantity {

    private final int quantity;

    private ProductQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static ProductQuantity from(int quantity) {
        validate(quantity);
        return new ProductQuantity(quantity);
    }

    private static void validate(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Invalid Quantity");
        }
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductQuantity productQuantity1 = (ProductQuantity) o;
        return quantity == productQuantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}

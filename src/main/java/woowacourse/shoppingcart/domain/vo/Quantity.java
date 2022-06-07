package woowacourse.shoppingcart.domain.vo;

import java.util.Objects;

public class Quantity {

    private final int quantity;

    private Quantity(int quantity) {
        this.quantity = quantity;
    }

    public static Quantity from(int quantity) {
        validate(quantity);
        return new Quantity(quantity);
    }

    private static void validate(int quantity) {
        if (quantity <= 0) {
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
        Quantity quantity1 = (Quantity) o;
        return quantity == quantity1.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}

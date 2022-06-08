package woowacourse.shoppingcart.domain.vo;

import java.util.Objects;

public class OrderQuantity {

    private final int quantity;

    private OrderQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static OrderQuantity from(int quantity) {
        validate(quantity);
        return new OrderQuantity(quantity);
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
        OrderQuantity that = (OrderQuantity) o;
        return quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }
}

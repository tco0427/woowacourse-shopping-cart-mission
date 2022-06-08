package woowacourse.shoppingcart.domain.vo;

import java.util.Objects;

public class Price {

    private final int price;

    private Price(int price) {
        this.price = price;
    }

    public static Price from(int price) {
        validate(price);
        return new Price(price);
    }

    private static void validate(int price) {
        if (price < 0) {
            throw new IllegalArgumentException("Invalid Price");
        }
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price price1 = (Price) o;
        return price == price1.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}

package woowacourse.shoppingcart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CartTest {

    @DisplayName("카트에 담는 항목의 크기는 0보다 작을 수 없다.")
    @Test
    public void cartSize() {
        // given
        final Image image = new Image("imageUrl", "imageAlteration");
        final Product product = new Product("상품", 10_000, 10, image);

        // when & then
        assertThatThrownBy(() -> new Cart(product, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

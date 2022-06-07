package woowacourse.shoppingcart.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderDetailTest {

    @DisplayName("주문 수량의 개수는 0보다 작거나 같을 수 없다.")
    @Test
    public void orderSize() {
        // given
        final Image image = new Image("imageUrl", "imageAlteration");
        final Product product = new Product("상품", 10_000, 10, image);

        // when & then
        assertThatThrownBy(() -> new OrderDetail(product, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

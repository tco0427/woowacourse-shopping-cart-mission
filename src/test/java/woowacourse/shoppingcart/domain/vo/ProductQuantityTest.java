package woowacourse.shoppingcart.domain.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductQuantityTest {

    @DisplayName("품절인 경우 수량이 0이므로 음수가 아니면 안된다.")
    @Test
    public void checkSize() {
        assertThatThrownBy(() -> ProductQuantity.from(-1)).isInstanceOf(IllegalArgumentException.class);
    }
}

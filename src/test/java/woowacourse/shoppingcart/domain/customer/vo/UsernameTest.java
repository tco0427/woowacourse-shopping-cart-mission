package woowacourse.shoppingcart.domain.customer.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsernameTest {

    @DisplayName("사용자 이름이 10자 보다 크면 생성이 불가능하다.")
    @Test
    public void checkPassword() {
        assertThatThrownBy(() -> Username.from("dwooooooooo"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("사용자 이름은 10자 이하여야 한다.")
    @Test
    public void createPassword() {
        assertDoesNotThrow(() -> Username.from("dwoo"));
    }
}

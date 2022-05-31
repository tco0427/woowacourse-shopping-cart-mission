package woowacourse.shoppingcart.domain.customer.vo;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailTest {

    @DisplayName("이메일은 형식을 지켜야한다.")
    @Test
    public void checkEmail() {
        assertThatThrownBy(() -> Email.from("email"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이메일은 형식을 지키면 제대로 생성된다.")
    @Test
    public void createEmail() {
        assertDoesNotThrow(() -> Email.from("email@email.com"));
    }
}

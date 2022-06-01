package woowacourse.shoppingcart.exception;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        this("존재하지 않는 유저입니다.");
    }

    public InvalidPasswordException(final String msg) {
        super(msg);
    }
}

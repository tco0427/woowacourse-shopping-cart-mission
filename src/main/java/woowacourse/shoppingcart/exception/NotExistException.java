package woowacourse.shoppingcart.exception;

public class NotExistException extends IllegalArgumentException {

    public NotExistException() {
        super();
    }

    public NotExistException(String message) {
        super(message);
    }
}

package woowacourse.shoppingcart.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.util.Arrays;
import woowacourse.shoppingcart.exception.NotExistException;

@JsonFormat(shape = Shape.OBJECT)
public enum ErrorResponse {

    DUPLICATED_EMAIL(1001, "Duplicated Email"),
    LOGIN_FAIL(2001, "Login Fail"),
    INCORRECT_PASSWORD(3001, "Incorrect Password"),
    INVALID_TOKEN(3002, "Invalid Token"),
    INVALID_EMAIL(4001, "Invalid Email"),
    INVALID_PASSWORD(4002, "Invalid Password"),
    INVALID_USERNAME(4003, "Invalid Username"),
    INVALID_PRICE(4004, "Invalid Price"),
    ALREADY_EXIST(5001, "Already Exist"),
    INVALID_QUANTITY(5002, "Invalid Quantity"),
    INVALID_CARTITEM(5003, "Invalid CartItem"),
    NOT_EXIST_PRODUCT(6001, "Not Exist Product"),
    NOT_EXIST_CARTITEM(6002, "Not Exist CartItem"),
    NOT_EXIST_ORDER(6003, "Not Exist Order"),
    NOT_EXIST_CUSTOMER(6004, "Not Exist Customer"),
    OUT_OF_STOCK(7001, "Out Of Stock");

    private final int errorCode;
    private final String message;

    ErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ErrorResponse from(String message) {
        return Arrays.stream(values())
                .filter(it -> it.getMessage().equals(message))
                .findAny()
                .orElseThrow(NotExistException::new);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}


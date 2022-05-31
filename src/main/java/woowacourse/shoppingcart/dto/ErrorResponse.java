package woowacourse.shoppingcart.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

@JsonFormat(shape = Shape.OBJECT)
public enum ErrorResponse {

    DUPLICATED_EMAIL(1001, "Duplicated Email"),
    INVALID_VALUE(1002, "Invalid Value");


    private int errorCode;
    private String message;

    ErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}


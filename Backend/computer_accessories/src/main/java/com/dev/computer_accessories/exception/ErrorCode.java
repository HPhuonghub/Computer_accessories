package com.dev.computer_accessories.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {


    // USER
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "Email already exists", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "Username not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INCORRECT_USERNAME_OR_PASSWORD(1009, "Incorrect username or password", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1010, "User not found", HttpStatus.NOT_FOUND),
    PASSWORD_MISMATCH(1011, "Password does not match", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_BLANK(1012, "Token must be not blank", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID(1013, "Token is invalid", HttpStatus.UNAUTHORIZED),

    // SUPPLIER
    SUPPLIER_NOT_FOUND(2000, "Supplier not found", HttpStatus.NOT_FOUND),
    SUPPLIER_EXISTED(2001, "Supplier already exists", HttpStatus.BAD_REQUEST),

    // CATEGORY
    CATEGORY_NOT_FOUND(3000, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_EXISTED(3001, "Category already exists", HttpStatus.BAD_REQUEST),

    // PRODUCT
    PRODUCT_NOT_FOUND(4000, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_EXISTED(4001, "Product already exists", HttpStatus.BAD_REQUEST),
    PRODUCT_QUANTITY_UNAVAILABLE(4002, "Not enough product stock", HttpStatus.BAD_REQUEST),



    // SPECIFICATION_PRODUCT
    SPECIFICATION_PRODUCT_NOT_FOUND(5000, "Specification product not found", HttpStatus.NOT_FOUND),
    SPECIFICATION_PRODUCT_EXISTED(5001, "Specification product already exists", HttpStatus.BAD_REQUEST),

    // ROLE
    ROLE_NOT_FOUND(6000, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_EXISTED(6001, "Role already exists", HttpStatus.BAD_REQUEST),

    // ORDERS
    ORDERS_NOT_FOUND(7000, "Orders not found", HttpStatus.NOT_FOUND),
    ORDERS_EXISTED(7001, "Orders already exists", HttpStatus.BAD_REQUEST),

    // ORDER_DETAILS
    ORDER_DETAILS_NOT_FOUND(8000, "OrderDetails not found", HttpStatus.NOT_FOUND),
    ORDER_DETAILS_EXISTED(8001, "OrderDetails already exists", HttpStatus.BAD_REQUEST),

    // FEEDBACK
    FEEDBACK_NOT_FOUND(9000, "Feedback not found", HttpStatus.NOT_FOUND),

    // CART
    CART_NOT_FOUND(10000, "Cart not found", HttpStatus.NOT_FOUND),

    // NOT FOUND
    NOT_FOUND(404, "Not found", HttpStatus.NOT_FOUND),

    // BAD REQUEST
    BAD_REQUEST(400, "Bad request", HttpStatus.BAD_REQUEST),

    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

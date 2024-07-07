package com.dev.computer_accessories.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception e, WebRequest request){
        ErrorResponse error = new ErrorResponse();
        error.setTimestamp(new Date());
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setPath(request.getDescription(false).replace("uri", ""));
        error.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());

        String message = e.getMessage();
        int start = message.lastIndexOf("[");
        int end = message.lastIndexOf("]") - 1;

        message = message.substring(start+1, end);

        error.setMessage(message);

        return error;
    }
}

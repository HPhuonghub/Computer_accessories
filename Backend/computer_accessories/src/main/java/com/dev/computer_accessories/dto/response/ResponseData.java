package com.dev.computer_accessories.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseData<T> {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    //PATCH,PUT,DELETE
    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    //GET,POST
    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


}

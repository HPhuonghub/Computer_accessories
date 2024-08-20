package com.dev.computer_accessories.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ErrorResponse {
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String error;
    private String message;

}

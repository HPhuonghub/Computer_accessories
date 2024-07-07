package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequest {


    private String fullname;

    @NotBlank(message = "username must be not null")
    private String username;

    @NotBlank(message = "fullname must be not null")
    private String password;

    private Role role;
}

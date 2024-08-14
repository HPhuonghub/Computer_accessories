package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignInRequest {


    private String fullname;

    @NotBlank(message = "username must be not null")
    private String username;

    @Size(min = 6, message = "Password must be more than 6 characters")
    private String password;

    private Role role;
}

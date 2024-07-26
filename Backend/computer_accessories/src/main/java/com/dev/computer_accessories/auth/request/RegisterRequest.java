package com.dev.computer_accessories.auth.request;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String email;
    private String password;
    private String role;
}

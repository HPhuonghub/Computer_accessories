package com.dev.computer_accessories.auth.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
}

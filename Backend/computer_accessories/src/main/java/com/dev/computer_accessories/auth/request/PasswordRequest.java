package com.dev.computer_accessories.auth.request;

import jakarta.validation.constraints.Size;
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

    @Size(min = 6, message = "Password must be longer than 6 characters")
    private String newPassword;
}

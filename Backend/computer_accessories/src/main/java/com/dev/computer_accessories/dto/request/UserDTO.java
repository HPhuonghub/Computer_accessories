package com.dev.computer_accessories.dto.request;


import com.dev.computer_accessories.model.Role;
import com.dev.computer_accessories.util.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {


    private Long id;

    @NotBlank(message = "Field fullName must be not blank")
    private String fullName;

    @Email(message = "Email invalid format")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Phone invalid format")
    private String phone;

    @NotNull(message = "Address must not null")
    private String address;

    @NotBlank(message = "Field password must not blank")
    private String password;

    private Role role ;

    private UserStatus status;


}

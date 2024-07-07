package com.dev.computer_accessories.dto.response;

import com.dev.computer_accessories.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
public class UserDetailResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private Role role;

}

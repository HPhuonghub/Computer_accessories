package com.dev.computer_accessories.dto.response;


import com.dev.computer_accessories.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponse {

    private String accessToken;

    private String refreshToken;

    private int status;

    private String message;

    private String email;

    private String fullname;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;

    private Role role;
}

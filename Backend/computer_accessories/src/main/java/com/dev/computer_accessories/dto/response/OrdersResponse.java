package com.dev.computer_accessories.dto.response;

import com.dev.computer_accessories.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrdersResponse {
    private long id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String note;
    private int status;
    private User user;
}

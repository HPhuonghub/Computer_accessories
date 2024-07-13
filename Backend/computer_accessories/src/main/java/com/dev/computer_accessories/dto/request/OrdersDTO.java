package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.User;
import lombok.Getter;

@Getter
public class OrdersDTO {
    private long id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String note;
    private int status;
    private User user;
}

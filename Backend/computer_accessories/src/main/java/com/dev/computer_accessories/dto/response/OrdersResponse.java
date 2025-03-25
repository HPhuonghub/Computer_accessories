package com.dev.computer_accessories.dto.response;

import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.util.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

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
    private OrderStatus status;
    private User user;
    private List<OrderDetails> orderDetails;
}

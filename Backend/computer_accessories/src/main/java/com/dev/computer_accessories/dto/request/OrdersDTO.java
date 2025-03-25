package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.PaymentMethod;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.util.OrderStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class OrdersDTO {
    private long id;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private String note;
    private List<OrderDetails> orderDetailsList;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private User user;
}

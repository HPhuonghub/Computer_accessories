package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.Orders;
import lombok.Getter;

import java.util.List;

@Getter
public class CheckoutRequest {
    private long id;
    private OrdersDTO ordersDTO;
    private List<OrderDetailsDTO> orderDetailsDTOS;
}

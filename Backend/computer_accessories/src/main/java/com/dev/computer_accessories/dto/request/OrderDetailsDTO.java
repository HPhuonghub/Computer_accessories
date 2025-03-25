package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.Orders;
import com.dev.computer_accessories.model.PaymentMethod;
import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.util.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDetailsDTO {
    private long id;
    private double price;
    private int quantity;
    private Orders orders;
    private Product product;
}

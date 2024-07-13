package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.Orders;
import com.dev.computer_accessories.model.Product;
import lombok.Getter;

@Getter
public class OrderDetailsDTO {
    private long id;
    private double price;
    private int quantity;
    private Orders orders;
    private Product product;
}

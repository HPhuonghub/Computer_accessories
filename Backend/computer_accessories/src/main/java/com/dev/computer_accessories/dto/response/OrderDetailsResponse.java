package com.dev.computer_accessories.dto.response;

import com.dev.computer_accessories.model.Orders;
import com.dev.computer_accessories.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderDetailsResponse {
    private long id;
    private double price;
    private int quantity;
    private Orders orders;
    private Product product;
}

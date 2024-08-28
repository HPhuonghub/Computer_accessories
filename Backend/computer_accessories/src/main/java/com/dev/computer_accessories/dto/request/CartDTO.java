package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import lombok.Getter;

import java.util.List;

@Getter
public class CartDTO {
    private int id;
    private List<Product> products;
    private User user;
}

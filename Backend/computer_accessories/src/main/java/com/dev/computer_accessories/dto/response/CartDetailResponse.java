package com.dev.computer_accessories.dto.response;

import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Builder
public class CartDetailResponse {
    private int id;
    private List<Product> products;
    private User user;
    private Date createdAt;
}

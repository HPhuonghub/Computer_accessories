package com.dev.computer_accessories.dto.response;


import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedBackDetailResponse {
    private int id;
    private int rating;
    private String comment;
    private User user;
    private Product product;
}

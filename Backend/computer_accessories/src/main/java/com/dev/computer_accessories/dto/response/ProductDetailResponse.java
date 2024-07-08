package com.dev.computer_accessories.dto.response;

import com.dev.computer_accessories.model.Category;
import com.dev.computer_accessories.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ProductDetailResponse {

    private long id;
    private String name;
    private String description;
    private String thumbnail;
    private double price;
    private int stock;
    private int discount;
    private Category category;
    private Supplier supplier;
}

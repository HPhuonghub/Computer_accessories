package com.dev.computer_accessories.dto.response;


import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSpecificationDetailResponse {
    private long id;
    private String specName;
    private String specValue;
    private Product product;
}

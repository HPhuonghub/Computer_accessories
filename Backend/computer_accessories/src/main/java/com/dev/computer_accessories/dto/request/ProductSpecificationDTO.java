package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSpecificationDTO {
    private long id;
    private String specName;
    private String specValue;
    private Product product;
}

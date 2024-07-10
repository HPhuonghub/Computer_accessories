package com.dev.computer_accessories.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDTO {
    private int id;
    private String name;
    private String description;
}

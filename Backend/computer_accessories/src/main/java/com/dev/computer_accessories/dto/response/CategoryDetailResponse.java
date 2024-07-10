package com.dev.computer_accessories.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDetailResponse {
    private int id;
    private String name;
    private String description;
}

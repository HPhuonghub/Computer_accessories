package com.dev.computer_accessories.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SupplierDetailResponse {
    private long id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String description;
}

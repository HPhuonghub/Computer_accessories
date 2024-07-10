package com.dev.computer_accessories.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDTO {
    private int id;

    @NotBlank(message = "Supplier name must be not blank")
    private String name;

    @NotBlank(message = "Supplier address must be not blank")
    private String address;

    @NotBlank(message = "Supplier phone must be not blank")
    private String phone;

    @NotBlank(message = "Supplier email must be not blank")
    private String email;

    private String description;
}

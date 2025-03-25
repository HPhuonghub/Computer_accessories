package com.dev.computer_accessories.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentMethodDTO {
    @NotNull(message = "Payment Method ID cannot be null")
    private int id;

    @NotBlank(message = "Payment Method name cannot be blank")
    @Size(min = 2, max = 100, message = "Payment Method name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Payment Method type cannot be blank")
    @Size(min = 2, max = 50, message = "Payment Method type must be between 2 and 50 characters")
    private String type;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}

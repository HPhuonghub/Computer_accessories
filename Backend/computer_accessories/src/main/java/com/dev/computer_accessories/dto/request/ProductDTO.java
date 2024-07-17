package com.dev.computer_accessories.dto.request;

import com.dev.computer_accessories.model.Category;
import com.dev.computer_accessories.model.ProductSpecification;
import com.dev.computer_accessories.model.Supplier;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {

    private Long id;

    @NotBlank(message = "Product must be not blank")
    private String name;

    private String description;

    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private long price;

    @Min(value = 0, message = "Discount must be greater than or equal to 0")
    private int discount;

    @NotBlank(message = "Thumbnail must be not blank")
    private String thumbnail;

    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private int stock;

    private Category category;

    private Supplier supplier;

    private List<ProductSpecification> productSpecifications;
}

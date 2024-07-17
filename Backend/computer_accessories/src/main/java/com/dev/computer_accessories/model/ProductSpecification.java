package com.dev.computer_accessories.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "tbl_product_specification")
@Entity
public class ProductSpecification extends AbstractEntity<Long>{

    private String specName;

    private String specValue;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id")
    private Product product;
}

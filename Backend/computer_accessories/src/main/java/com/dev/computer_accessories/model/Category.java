package com.dev.computer_accessories.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "tbl_category")
@Entity
public class Category extends AbstractEntity<Integer>{
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
}

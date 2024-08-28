package com.dev.computer_accessories.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_cart")
@Entity(name = "Cart")
public class Cart extends AbstractEntity<Integer> {

    @OneToMany(mappedBy = "cart")
    @JsonIgnore
    private List<Product> products = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}

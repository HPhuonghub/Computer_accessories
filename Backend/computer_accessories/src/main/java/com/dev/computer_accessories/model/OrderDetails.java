package com.dev.computer_accessories.model;

import com.dev.computer_accessories.util.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_order_details")
@Entity
public class OrderDetails extends AbstractEntity<Long> {

//    @Column(name = "price")
//    private double price;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @OneToOne
    private Product product;
    
}

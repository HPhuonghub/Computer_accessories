package com.dev.computer_accessories.model;

import com.dev.computer_accessories.util.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_orders")
@Entity
public class Orders extends AbstractEntity<Long>{

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "note")
    private String note;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrderDetails> orderDetails = new ArrayList<>();

    @Enumerated(EnumType.STRING) //đảm bảo rằng giá trị của enum OrderStatus được lưu dưới dạng chuỗi trong cơ sở dữ liệu.
    private OrderStatus orderStatus;

    private double totalPrice;
}

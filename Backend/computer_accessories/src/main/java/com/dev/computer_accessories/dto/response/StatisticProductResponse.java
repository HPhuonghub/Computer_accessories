package com.dev.computer_accessories.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticProductResponse {
    private long id;
    private String name;
    private int quantity;
    private double totalPrice;
}

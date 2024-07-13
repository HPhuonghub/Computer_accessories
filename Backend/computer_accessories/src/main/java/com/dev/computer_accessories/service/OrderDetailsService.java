package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.OrderDetailsDTO;
import com.dev.computer_accessories.dto.response.OrderDetailsResponse;
import com.dev.computer_accessories.dto.response.PageResponse;

public interface OrderDetailsService {
    void saveOrderDetails(OrderDetailsDTO orderDetailsDTO);

    void updateOrderDetails(long id, OrderDetailsDTO orderDetailsDTO);

    void deleteOrderDetails(long id);

    OrderDetailsResponse getOrderDetails(long id);

    PageResponse<?> getAllOrderDetailsWithSortBy(int pageNo, int pageSize, String sortBy);
}

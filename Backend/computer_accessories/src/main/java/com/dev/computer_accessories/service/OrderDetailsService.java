package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.OrderDetailsDTO;
import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.OrderDetailsResponse;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.Orders;

import java.util.List;

public interface OrderDetailsService {
    void saveOrderDetails(OrderDetailsDTO orderDetailsDTO);

    void processOrder(OrdersDTO ordersDTO, List<OrderDetailsDTO> orderDetailsDTOS);

    void updateOrderDetails(long id, OrderDetailsDTO orderDetailsDTO);

    void deleteOrderDetails(long id);

    OrderDetailsResponse getOrderDetails(long id);

    List<OrderDetailsResponse> getOrderDetailByUserId(long id);

    PageResponse<?> getAllOrderDetailsWithSortBy(int pageNo, int pageSize, String sortBy);
}

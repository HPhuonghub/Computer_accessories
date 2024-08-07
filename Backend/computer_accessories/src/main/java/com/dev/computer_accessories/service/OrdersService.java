package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.OrdersResponse;
import com.dev.computer_accessories.dto.response.PageResponse;

public interface OrdersService {

    void saveOrders(OrdersDTO ordersDTO);

    void updateOrders(long id, OrdersDTO ordersDTO);

    void deleteOrders(long id);

    OrdersResponse getOrdersDetail(String email);

    PageResponse<?> getAllOrdersWithSortBy(int pageNo, int pageSize, String sortBy);
}

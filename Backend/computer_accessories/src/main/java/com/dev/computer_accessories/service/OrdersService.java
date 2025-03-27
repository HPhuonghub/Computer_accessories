package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.OrdersResponse;
import com.dev.computer_accessories.dto.response.PageResponse;

import java.util.List;

public interface OrdersService {

    void saveOrders(OrdersDTO ordersDTO);

    void updateOrders(long id, OrdersDTO ordersDTO);

    void updateStatusOrder(long id, String status);

    void deleteOrders(long id);

    OrdersResponse getOrderId(long id);

    List<OrdersResponse> getOrdersDetail(String email);

    PageResponse<?> getAllOrdersWithSortBy(int pageNo, int pageSize, String sortBy);
}

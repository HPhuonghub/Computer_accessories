package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.OrderDetailsDTO;
import com.dev.computer_accessories.dto.response.OrderDetailsResponse;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.Orders;
import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.repository.OrderDetailsRepository;
import com.dev.computer_accessories.service.OrderDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final OrdersServiceImpl ordersService;
    private final ProductServiceImpl productService;
    private final OrderDetailsRepository orderDetailsRepository;


    @Override
    public void saveOrderDetails(OrderDetailsDTO orderDetailsDTO) {
        Orders orders = ordersService.getOrdersByEmail(orderDetailsDTO.getOrders().getEmail());
        Product product = productService.getProductByName(orderDetailsDTO.getProduct().getName());

        OrderDetails orderDetails = OrderDetails.builder()
                .price(orderDetailsDTO.getPrice())
                .quantity(orderDetailsDTO.getQuantity())
                .orders(orders)
                .product(product)
                .build();

        orderDetailsRepository.save(orderDetails);
    }

    @Override
    public void updateOrderDetails(long id, OrderDetailsDTO orderDetailsDTO) {
        OrderDetails orderDetails = getOrderDetailsById(id);

        orderDetails.setPrice(orderDetailsDTO.getPrice());
        orderDetails.setQuantity(orderDetails.getQuantity());

        orderDetailsRepository.save(orderDetails);

    }

    @Override
    public void deleteOrderDetails(long id) {
        orderDetailsRepository.deleteById(id);
    }

    @Override
    public OrderDetailsResponse getOrderDetails(long id) {
        OrderDetails orderDetails = getOrderDetailsById(id);
        return OrderDetailsResponse.builder()
                .price(orderDetails.getPrice())
                .quantity(orderDetails.getQuantity())
                .product(orderDetails.getProduct())
                .orders(orderDetails.getOrders())
                .build();
    }

    @Override
    public PageResponse<?> getAllOrderDetailsWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if(pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));

        Page<OrderDetails> orderDetails = orderDetailsRepository.findAll(pageable);

        List<OrderDetailsResponse> res = orderDetails.stream().map(order -> OrderDetailsResponse.builder()
                        .price(order.getPrice())
                        .quantity(order.getQuantity())
                        .orders(order.getOrders())
                        .product(order.getProduct())
                        .id(order.getId())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(orderDetails.getTotalPages())
                .items(res)
                .build();
    }

    private OrderDetails getOrderDetailsById(long id) {
        return orderDetailsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("OrderDetails not found"));
    }

}

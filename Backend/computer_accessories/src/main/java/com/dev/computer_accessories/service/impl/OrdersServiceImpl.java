package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.OrdersResponse;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.exception.AppException;
import com.dev.computer_accessories.exception.ErrorCode;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.Orders;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.OrderDetailsRepository;
import com.dev.computer_accessories.repository.OrdersRepository;
import com.dev.computer_accessories.service.OrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {

    private final UserServiceImpl userService;
    private final OrdersRepository ordersRepository;
    private final OrderDetailsRepository orderDetailsRepository;


    @Override
    public void saveOrders(OrdersDTO ordersDTO) {
        User user = userService.getUserByEmail(ordersDTO.getUser().getEmail());

        Orders orders = Orders.builder()
                .fullname(ordersDTO.getFullname())
                .phone(ordersDTO.getPhone())
                .email(ordersDTO.getEmail())
                .note(ordersDTO.getNote())
                .address(ordersDTO.getAddress())
                .user(user)
                .build();

        ordersRepository.save(orders);
    }

    @Override
    public void updateOrders(long id, OrdersDTO ordersDTO) {
        Orders orders = getOrdersById(id);

        orders.setFullname(ordersDTO.getFullname());
        orders.setEmail(orders.getEmail());
        orders.setPhone(ordersDTO.getPhone());
        orders.setAddress(ordersDTO.getAddress());
        orders.setNote(ordersDTO.getNote());

        ordersRepository.save(orders);

    }

    @Override
    public void deleteOrders(long id) {
        ordersRepository.deleteById(id);
    }

    @Override
    public List<OrdersResponse> getOrdersDetail(String email) {
        List<Orders> orders = getOrdersByEmail(email);


        List<OrdersResponse> ordersResponses = new ArrayList<>();

        orders.stream().forEach(order -> {

                    List<OrderDetails> orderDetails = orderDetailsRepository.findAllByOrderId(order.getId()).get();

                    ordersResponses.add(OrdersResponse.builder()
                                    .id(order.getId())
                            .fullname(order.getFullname())
                            .email(order.getEmail())
                            .address(order.getAddress())
                            .phone(order.getPhone())
                            .status(order.getOrderStatus())
                            .orderDetails(orderDetails)
                            .build());
                }
        );
        return ordersResponses;
    }

    @Override
    public PageResponse<?> getAllOrdersWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if (pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) {
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

        Page<Orders> orders = ordersRepository.findAll(pageable);

        List<OrdersResponse> res = orders.stream().map(order -> OrdersResponse.builder()
                        .email(order.getEmail())
                        .fullname(order.getFullname())
                        .phone(order.getPhone())
                        .address(order.getAddress())
                        .note(order.getNote())
                        .user(order.getUser())
                        .id(order.getId())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(orders.getTotalPages())
                .items(res)
                .build();
    }

    private Orders getOrdersById(long id) {
        return ordersRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_FOUND));
    }

    public List<Orders> getOrdersByEmail(String email) {
        return ordersRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.ORDERS_NOT_FOUND));
    }

}

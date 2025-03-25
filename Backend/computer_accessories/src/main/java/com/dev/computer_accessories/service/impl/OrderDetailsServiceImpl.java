package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.OrderDetailsDTO;
import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.OrderDetailsResponse;
import com.dev.computer_accessories.dto.response.OrdersResponse;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.exception.AppException;
import com.dev.computer_accessories.exception.ErrorCode;
import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.Orders;
import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.OrderDetailsRepository;
import com.dev.computer_accessories.repository.OrdersRepository;
import com.dev.computer_accessories.repository.ProductRepository;
import com.dev.computer_accessories.service.OrderDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class OrderDetailsServiceImpl implements OrderDetailsService {
    private final OrdersServiceImpl ordersService;
    private final ProductServiceImpl productService;
    private final UserServiceImpl userService;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;


    @Override
    public void saveOrderDetails(OrderDetailsDTO orderDetailsDTO) {
        Orders orders = ordersRepository.findById(orderDetailsDTO.getOrders().getId()).get();
        Product product = productService.getProductByName(orderDetailsDTO.getProduct().getName());

        OrderDetails orderDetails = OrderDetails.builder()
                .quantity(orderDetailsDTO.getQuantity())
                .orders(orders)
                .product(product)
                .build();

        orderDetailsRepository.save(orderDetails);
    }

    @Transactional
    public void processOrder(OrdersDTO ordersDTO, List<OrderDetailsDTO> orderDetailsDTOS) {
//        System.out.println("Check OrdersDTO" + ordersDTO.toString());
//        for(OrderDetailsDTO orderDetailsDTO : orderDetailsDTOS) {
//            System.out.println("Check OrderDetailsDTO" + orderDetailsDTO.toString());
//        }
        User user = userService.getUserByEmail(ordersDTO.getUser().getEmail());
        Orders orders = Orders.builder()
                .fullname(ordersDTO.getFullname())
                .phone(ordersDTO.getPhone())
                .email(ordersDTO.getEmail())
                .note(ordersDTO.getNote())
                .address(ordersDTO.getAddress())
                .user(user)
                .build();
        Orders order = ordersRepository.save(orders);
        long orderId = order.getId();

        for (OrderDetailsDTO detail : orderDetailsDTOS) {
            Optional<Product> product = productRepository.findById(detail.getProduct().getId());
            if (product.get().getStock() < detail.getQuantity()) {
                throw new AppException(ErrorCode.PRODUCT_QUANTITY_UNAVAILABLE);
            }
            // Cập nhật số lượng sản phẩm
            product.get().setStock((product.get().getStock() - detail.getQuantity()));
            Product product1 = productRepository.save(product.get()); // Lưu thay đổi

            Orders orderById = getOrdersById(orderId);
            Product productBy = getProductById(product1.getId());

            OrderDetails orderDetails = OrderDetails.builder()
                    .quantity(detail.getQuantity())
                    .orders(orderById)
                    .product(productBy)
                    .build();

            orderDetailsRepository.save(orderDetails); // Lưu chi tiết đơn hàng vào DB
        }
    }

    @Override
    public void updateOrderDetails(long id, OrderDetailsDTO orderDetailsDTO) {
        OrderDetails orderDetails = getOrderDetailsById(id);

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
                .quantity(orderDetails.getQuantity())
                .product(orderDetails.getProduct())
                .orders(orderDetails.getOrders())
                .build();
    }

    @Override
    public List<OrderDetailsResponse> getOrderDetailByUserId(long id) {
        List<Orders> orders = getAllOrdersByUserId(id);
        List<OrderDetailsResponse> orderDetailsResponses = new ArrayList<>();
        List<OrdersResponse> ordersResponses = new ArrayList<>();

        for (Orders order : orders) {
            ordersResponses.add(OrdersResponse.builder()
                    .fullname(order.getFullname())
                    .email(order.getEmail())
                    .address(order.getAddress())
                    .phone(order.getPhone())
                    .id(order.getId())
                    .build())
            ;
        }
        for (OrdersResponse orderResponse : ordersResponses) {
            List<OrderDetails> orderDetails = getOrderDetailsByOrderId(orderResponse.getId());
            for (OrderDetails orderDetail : orderDetails) {
                orderDetailsResponses.add(OrderDetailsResponse.builder()
                        .quantity(orderDetail.getQuantity())
                        .orders(orderDetail.getOrders())
                        .product(orderDetail.getProduct())
                        .id(orderDetail.getId())
                        .build());
            }

        }
        return orderDetailsResponses;
    }

    @Override
    public PageResponse<?> getAllOrderDetailsWithSortBy(int pageNo, int pageSize, String sortBy) {
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

        Page<OrderDetails> orderDetails = orderDetailsRepository.findAll(pageable);

        List<OrderDetailsResponse> res = orderDetails.stream().map(order -> OrderDetailsResponse.builder()
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
        return orderDetailsRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAILS_NOT_FOUND));
    }

    private List<OrderDetails> getOrderDetailsByOrderId(long id) {
        return orderDetailsRepository.findAllByOrderId(id).orElseThrow(() -> new AppException(ErrorCode.ORDER_DETAILS_NOT_FOUND));
    }

    private Orders getOrdersById(long id) {
        return ordersRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ORDERS_EXISTED));
    }

    private List<Orders> getAllOrdersByUserId(long id) {
        return ordersRepository.findAllByUserId(id).orElseThrow(() -> new AppException(ErrorCode.ORDERS_EXISTED));
    }

    private Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
    }
}

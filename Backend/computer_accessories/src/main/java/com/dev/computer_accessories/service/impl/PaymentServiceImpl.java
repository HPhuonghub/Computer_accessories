package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.OrderDetailsDTO;
import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.WebhookResponseDTO;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.Orders;
import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.OrderDetailsRepository;
import com.dev.computer_accessories.repository.OrdersRepository;
import com.dev.computer_accessories.repository.ProductRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.PaymentService;
import com.dev.computer_accessories.util.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.Webhook;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PayOS payOS;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderDetailsRepository orderDetailsRepository;
    private final OrdersRepository ordersRepository;
//    private final EmailSenderService emailSenderService;

    @Transactional
    @Override
    public String createPaymentLink(OrdersDTO ordersDTO) throws Exception{
        // B1: Lấy thông tin người dùng
        Optional<User> user = Optional.ofNullable(
                userRepository.findByEmail(ordersDTO.getUser().getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("Email is not found"))
        );

        // B2: Tạo hóa đơn
        double totalPrice = ordersDTO.getOrderDetailsList().stream()
                .mapToDouble(product -> product.getProduct().getPrice() * product.getQuantity())
                .sum();

        Orders orders = ordersRepository.save(
                Orders.builder()
                        .user(userRepository.findByEmail(ordersDTO.getUser().getEmail()).orElseThrow(() -> new ResourceNotFoundException("Email is not found")))
                        .email(ordersDTO.getEmail())
                        .phone(ordersDTO.getPhone())
                        .address(ordersDTO.getAddress())
                        .note(ordersDTO.getNote())
                        .fullname(ordersDTO.getFullname())
                        .orderStatus(OrderStatus.PENDING)
                        .totalPrice(totalPrice)
                        .build());

        // B3: Tạo danh sách ItemData từ Orders
        List<ItemData> itemDataList = new ArrayList<>();

        ordersDTO.getOrderDetailsList().stream().forEach(orderDetails -> {

            // Lấy sản phẩm từ repository
            Product product = productRepository.findById(orderDetails.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product is not found"));

            // Kiểm tra số lượng yêu cầu có vượt quá tồn kho hay không
            if (orderDetails.getQuantity() > product.getStock()) {
                throw new ResourceNotFoundException("Quantity for " + product.getName() + " is greater than stock");
            }

            itemDataList.add(
                    ItemData.builder()
                            .name(orderDetails.getProduct().getName())
                            .price((int) orderDetails.getProduct().getPrice())
                            .quantity(orderDetails.getQuantity())
                            .build()
            );

            // Lưu OrderDetails
            orderDetailsRepository.save(OrderDetails.builder()
                    .quantity(orderDetails.getQuantity())
                    .orders(orders)
                    .product(orderDetails.getProduct())
                    .build());
        });

        // B4: Tạo PaymentData với danh sách ItemData
        PaymentData paymentData = PaymentData.builder()
                .orderCode((long) orders.getId())
                .amount((int) totalPrice) // Tổng giá trị hóa đơn
                .description("Payment for Invoice #" + orders.getId())
                .returnUrl("http://localhost:3000/view-checkout/" + user.get().getId()) // URL khi thanh toán thành công
                .cancelUrl("http://localhost:3000")        // URL khi hủy thanh toán
                .items(itemDataList) // Danh sách các ItemData
                .build();

        // B5: Tạo liên kết thanh toán
        CheckoutResponseData result = payOS.createPaymentLink(paymentData);

        // Trả về liên kết thanh toán
        return result.getCheckoutUrl();
    }

    @Transactional
    @Override
    public void paymentCOD(OrdersDTO ordersDTO) {
        System.out.println("ordersDTO : " + ordersDTO.toString());
        // B1: Lấy thông tin người dùng
        Optional<User> user = Optional.ofNullable(
                userRepository.findByEmail(ordersDTO.getUser().getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("Email is not found"))
        );

        // B2: Tạo hóa đơn
        double totalPrice = ordersDTO.getOrderDetailsList().stream()
                .mapToDouble(product -> product.getProduct().getPrice() * product.getQuantity())
                .sum();

        Orders orders = ordersRepository.save(
                Orders.builder()
                        .user(userRepository.findByEmail(ordersDTO.getUser().getEmail()).orElseThrow(() -> new ResourceNotFoundException("Email is not found")))
                        .email(ordersDTO.getEmail())
                        .phone(ordersDTO.getPhone())
                        .address(ordersDTO.getAddress())
                        .note(ordersDTO.getNote())
                        .fullname(ordersDTO.getFullname())
                        .orderStatus(OrderStatus.PENDING)
                        .totalPrice(totalPrice)
                        .build());

        // B3: Kiểm tra số lượng yêu cầu có vượt quá tồn kho
        for (OrderDetails orderDetails : ordersDTO.getOrderDetailsList()) {
            // Kiểm tra xem sản phẩm có null hay không
            Product product = orderDetails.getProduct();
            if (product == null) {
                throw new ResourceNotFoundException("Product is null for order detail");
            }

            // Lấy sản phẩm từ repository
            product = productRepository.findById(product.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product is not found"));

            // Kiểm tra số lượng yêu cầu có vượt quá tồn kho hay không
            if (orderDetails.getQuantity() > product.getStock()) {
                throw new ResourceNotFoundException("Quantity for " + product.getName() + " is greater than stock");
            }

            // Lưu OrderDetails
            orderDetailsRepository.save(OrderDetails.builder()
                    .quantity(orderDetails.getQuantity())
                    .orders(orders)
                    .product(orderDetails.getProduct())
                    .build());

            // Trừ tồn kho sản phẩm
            product.setStock(product.getStock() - orderDetails.getQuantity());

            // Lưu sản phẩm đã cập nhật
            productRepository.save(product);
        }

    }


    public WebhookResponseDTO processWebhook(Webhook webhookData){
        System.out.println(webhookData.getData());
        if (webhookData.getSuccess() != null) {
             Orders orders = ordersRepository.findById(webhookData.getData().getOrderCode()).get();
             orders.setOrderStatus(OrderStatus.SHIPPED);
             ordersRepository.save(orders);

             List<OrderDetails> orderDetails = orderDetailsRepository.findAllByOrderId(orders.getId()).get();
             orderDetails.forEach(detail -> {
                 Product product = productRepository.findById(detail.getProduct().getId()).get();
                 product.setStock(product.getStock() - detail.getQuantity());
                 productRepository.save(product);
             });
        }

        return WebhookResponseDTO.builder()
                .success(Boolean.TRUE.equals(webhookData.getSuccess()))
                .build();
    }
}

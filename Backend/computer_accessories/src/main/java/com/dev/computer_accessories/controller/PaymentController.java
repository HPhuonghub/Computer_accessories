package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.OrderDetailsDTO;
import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.WebhookResponseDTO;
import com.dev.computer_accessories.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.type.Webhook;

@RestController
@RequestMapping("api/v1/payment")
@Tag(name = "Payment Controller")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-payment-link")
    public ResponseEntity<String> createPaymentLink(@RequestBody OrdersDTO ordersDTO) throws Exception {
        String checkoutUrl = paymentService.createPaymentLink(ordersDTO);
        return ResponseEntity.ok(checkoutUrl);
    }

    @PostMapping("/payment-cod")
    public ResponseData<?> paymentCOD(@RequestBody OrdersDTO ordersDTO) throws Exception {
        paymentService.paymentCOD(ordersDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Create a order successful");
    }

    @PostMapping("/receive-webhook")
    public WebhookResponseDTO receiveWebhook(@RequestBody Webhook webhookData) throws Exception {
        return paymentService.processWebhook(webhookData);
    }
}

package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.WebhookResponseDTO;
import org.springframework.stereotype.Service;
import vn.payos.type.Webhook;

@Service
public interface PaymentService {
    String createPaymentLink(OrdersDTO ordersDTO) throws Exception;

    void paymentCOD(OrdersDTO ordersDTO);

    WebhookResponseDTO processWebhook(Webhook webhookData);
}


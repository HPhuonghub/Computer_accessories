package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.PaymentMethodDTO;
import com.dev.computer_accessories.dto.response.PageResponse;

public interface PaymentMethodService {
    void save(PaymentMethodDTO paymentMethodDTO);

    void update(int paymentMethodId, PaymentMethodDTO paymentMethodDTO);

    void delete(int paymentMethodId);

    PaymentMethodDTO getPaymentMethodDTOById(int paymentMethodId);

    PageResponse<?> getAllPaymentMethodsWithSortBy(int pageNo, int pageSize, String sortBy);
}

package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.PaymentMethodDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.service.PaymentMethodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment Method Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment-methods")
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    @Operation(summary = "Add payment method", description = "API create new payment method")
    @PostMapping("/")
    public ResponseData<?> addPaymentMethod(@RequestBody @Valid PaymentMethodDTO paymentMethodDTO) {
        try {
            paymentMethodService.save(paymentMethodDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Add payment method successful");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Update a payment method", description = "API update the payment method")
    @PutMapping("/{paymentMethodId}")
    public ResponseData<?> updatePaymentMethod(@PathVariable int paymentMethodId, @RequestBody @Valid PaymentMethodDTO paymentMethodDTO) {
            paymentMethodService.update(paymentMethodId, paymentMethodDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Update payment method successful");
    }

    @Operation(summary = "Delete a payment method", description = "API delete a payment method")
    @DeleteMapping("/{paymentMethodId}")
    public ResponseData<?> deletePaymentMethod(@PathVariable @Valid int paymentMethodId) {
            paymentMethodService.delete(paymentMethodId);
            return new ResponseData<>(HttpStatus.NO_CONTENT.value(), "Delete payment method successful");
    }

    @Operation(summary = "Get payment method by id", description = "API return payment method by id")
    @GetMapping("/{paymentMethodId}")
    public ResponseData<?> getPaymentMethodById(@PathVariable @Valid int paymentMethodId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get payment method by id successful",  paymentMethodService.getPaymentMethodDTOById(paymentMethodId));
    }

    @Operation(summary = "Get list paymentMethods with sort", description = "API return list payment method with sort")
    @GetMapping("/list-sort")
    public ResponseData<?> getAllMovieWithSort(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                               @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                               @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all payment method successful", paymentMethodService.getAllPaymentMethodsWithSortBy(pageNo, pageSize, sortBy));
    }
}

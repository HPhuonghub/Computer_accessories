package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.CheckoutRequest;
import com.dev.computer_accessories.dto.request.OrderDetailsDTO;
import com.dev.computer_accessories.dto.response.OrderDetailsResponse;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.service.OrderDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-details")
@Tag(name = "OrderDetails Controller")
@Slf4j
public class OrderDetailsController {
    private final OrderDetailsService orderDetailsService;

    @Operation(summary = "Add orderDetails", description = "Api create new orderDetails")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostAuthorize("returnObject.data.orders.email == authentication.name")
    @PostMapping("/")
    public ResponseData<OrderDetailsResponse> saveOrderDetails(@RequestBody CheckoutRequest request) {
        try {
            orderDetailsService.processOrder(request.getOrdersDTO(), request.getOrderDetailsDTOS());
            return new ResponseData<>(HttpStatus.OK.value(), "OrderDetails added successfully");
        } catch (Exception e) {
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to add OrderDetails");
        }
    }


    @Operation(summary = "Delete orderDetails", description = "Api delete a orderDetails")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
//    @PostAuthorize("returnObject.data.orders.email == authentication.name")
    @DeleteMapping("/{orderDetailsId}")
    public ResponseData<OrderDetailsResponse> deleteOrderDetails(@PathVariable int orderDetailsId) {
        orderDetailsService.deleteOrderDetails(orderDetailsId);
        return new ResponseData<>(HttpStatus.OK.value(), "OrderDetails deleted successfully");
    }

    @Operation(summary = "Update orderDetails", description = "Api update a orderDetails")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostAuthorize("returnObject.data.orders.email == authentication.name")
    @PutMapping("/{orderDetailsId}")
    public ResponseData<OrderDetailsResponse> updateOrderDetails(@Min(value = 1, message = "orderDetailsId must be greater than 0") @PathVariable int orderDetailsId, @RequestBody OrderDetailsDTO orderDetailsDTO) {
        orderDetailsService.updateOrderDetails(orderDetailsId, orderDetailsDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "OrderDetails updated successfully");
    }

    @Operation(summary = "Get list of orderDetailss per page", description = "Send a request via this API to get orderDetails list by pageNo and pageSize")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/list")
    public ResponseData<?> getAllOrderDetails(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                              @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                              @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all orderDetails successful", orderDetailsService.getAllOrderDetailsWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get orderDetails by id", description = "Api get a orderDetails by id")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostAuthorize("returnObject.data.orders.email == authentication.name")
    @GetMapping("/{orderDetailsId}")
    public ResponseData<OrderDetailsResponse> getOrderDetailsById(@Min(value = 1, message = "orderDetailsId must be greater than 0") @PathVariable int orderDetailsId) {
        orderDetailsService.getOrderDetails(orderDetailsId);
        return new ResponseData<>(HttpStatus.OK.value(), "Get orderDetails by id successfully");
    }

    @Operation(summary = "Get orderDetails by id", description = "Api get a orderDetails by id")
//    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
//    @PostAuthorize("returnObject.data.orders.email == authentication.name")
    @GetMapping("/list/{userId}")
    public ResponseData<List<OrderDetailsResponse>> getOrderDetailsByUserId(@Min(value = 1, message = "orderDetailsId must be greater than 0") @PathVariable int userId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get orderDetails by id successfully", orderDetailsService.getOrderDetailByUserId(userId));
    }
}

package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.OrderDetailsDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.ResponseError;
import com.dev.computer_accessories.service.OrderDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-details")
@Tag(name="OrderDetails Controller")
@Slf4j
public class OrderDetailsController {
    private final OrderDetailsService orderDetailsService;

    @Operation(summary = "Add orderDetails", description = "Api create new orderDetails")
    @PostMapping("/")
    public ResponseData<?> saveOrderDetails(@RequestBody OrderDetailsDTO orderDetailsDTO) {
        try {
            orderDetailsService.saveOrderDetails(orderDetailsDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "OrderDetails added successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Operation(summary = "Delete orderDetails", description = "Api delete a orderDetails")
    @DeleteMapping("/{orderDetailsId}")
    public ResponseData<?> deleteOrderDetails(@PathVariable int orderDetailsId) {
        try {
            orderDetailsService.deleteOrderDetails(orderDetailsId);
            return new ResponseData<>(HttpStatus.OK.value(), "OrderDetails deleted successfully");
        } catch (Exception e) {
            log.error("Error Message={}, Cause={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Update orderDetails", description = "Api update a orderDetails")
    @PutMapping("/{orderDetailsId}")
    public ResponseData<?> updateOrderDetails(@Min(value = 1, message = "orderDetailsId must be greater than 0") @PathVariable int orderDetailsId, @RequestBody OrderDetailsDTO orderDetailsDTO) {
        try {
            orderDetailsService.updateOrderDetails(orderDetailsId, orderDetailsDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "OrderDetails updated successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list of orderDetailss per page", description = "Send a request via this API to get orderDetails list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllOrderDetails(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                        @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                        @RequestParam(required = false) String sortBy
    ) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all orderDetails successful", orderDetailsService.getAllOrderDetailsWithSortBy(pageNo, pageSize, sortBy));
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get orderDetails by id", description = "Api get a orderDetails by id")
    @GetMapping("/{orderDetailsId}")
    public ResponseData<?> getOrderDetailsById(@Min(value = 1, message = "orderDetailsId must be greater than 0") @PathVariable int orderDetailsId) {
        try {
            orderDetailsService.getOrderDetails(orderDetailsId);
            return new ResponseData<>(HttpStatus.OK.value(), "Get orderDetails by id successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}

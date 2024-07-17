package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.ResponseError;
import com.dev.computer_accessories.service.OrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Tag(name="Orders Controller")
@Slf4j
public class OrdersController {
    private final OrdersService ordersService;

    @Operation(summary = "Add orders", description = "Api create new orders")
    @PostMapping("/")
    public ResponseData<?> saveOrders(@RequestBody OrdersDTO ordersDTO) {
        try {
            ordersService.saveOrders(ordersDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Orders added successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Operation(summary = "Delete orders", description = "Api delete a orders")
    @DeleteMapping("/{ordersId}")
    public ResponseData<?> deleteOrders(@PathVariable int ordersId) {
        try {
            ordersService.deleteOrders(ordersId);
            return new ResponseData<>(HttpStatus.OK.value(), "Orders deleted successfully");
        } catch (Exception e) {
            log.error("Error Message={}, Cause={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Update orders", description = "Api update a orders")
    @PutMapping("/{ordersId}")
    public ResponseData<?> updateOrders(@Min(value = 1, message = "ordersId must be greater than 0") @PathVariable int ordersId, @RequestBody OrdersDTO ordersDTO) {
        try {
            ordersService.updateOrders(ordersId, ordersDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Orders updated successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list of orderss per page", description = "Send a request via this API to get orders list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllOrders(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                          @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                          @RequestParam(required = false) String sortBy
    ) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all orders successful", ordersService.getAllOrdersWithSortBy(pageNo, pageSize, sortBy));
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get orders by id", description = "Api get a orders by id")
    @GetMapping("/{ordersId}")
    public ResponseData<?> getOrdersById(@Min(value = 1, message = "ordersId must be greater than 0") @PathVariable int ordersId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get orders by id successfully", ordersService.getOrdersDetail(ordersId));
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}

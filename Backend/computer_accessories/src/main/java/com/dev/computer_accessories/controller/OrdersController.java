package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.OrdersDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
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
            ordersService.saveOrders(ordersDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Orders added successfully");
    }


    @Operation(summary = "Delete orders", description = "Api delete a orders")
    @DeleteMapping("/{ordersId}")
    public ResponseData<?> deleteOrders(@PathVariable int ordersId) {
            ordersService.deleteOrders(ordersId);
            return new ResponseData<>(HttpStatus.OK.value(), "Orders deleted successfully");
    }

    @Operation(summary = "Update orders", description = "Api update a orders")
    @PutMapping("/{ordersId}")
    public ResponseData<?> updateOrders(@Min(value = 1, message = "ordersId must be greater than 0") @PathVariable int ordersId, @RequestBody OrdersDTO ordersDTO) {
            ordersService.updateOrders(ordersId, ordersDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Orders updated successfully");
    }

    @Operation(summary = "Get list of orders per page", description = "Send a request via this API to get orders list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllOrders(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                          @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                          @RequestParam(required = false) String sortBy
    ) {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all orders successful", ordersService.getAllOrdersWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get orders by email", description = "Api get a orders by email")
    @GetMapping("/{email}")
    public ResponseData<?> getOrdersByEmail(@PathVariable String email) {
            return new ResponseData<>(HttpStatus.OK.value(), "Get orders by email successfully", ordersService.getOrdersDetail(email));
    }
}

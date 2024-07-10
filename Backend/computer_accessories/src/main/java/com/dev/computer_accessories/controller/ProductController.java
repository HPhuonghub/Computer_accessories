package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.ProductDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.ResponseError;
import com.dev.computer_accessories.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@Tag(name="Product Controller")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Add product", description = "Api create new product")
    @PostMapping("/")
    public ResponseData<?> saveProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.saveProduct(productDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Product added successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Operation(summary = "Delete product", description = "Api delete a product")
    @DeleteMapping("/{productId}")
    public ResponseData<?> deleteProduct(@PathVariable long productId) {
        try {
            productService.deleteProduct(productId);
            return new ResponseData<>(HttpStatus.OK.value(), "Product deleted successfully");
        } catch (Exception e) {
            log.error("Error Message={}, Cause={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Update product", description = "Api update a product")
    @PutMapping("/{productId}")
    public ResponseData<?> updateProduct(@Min(value = 1, message = "productId must be greater than 0") @PathVariable long productId, @RequestBody ProductDTO productDTO) {
        try {
            productService.updateProduct(productId, productDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Product updated successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list of products per page", description = "Send a request via this API to get product list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllProduct(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                         @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                         @RequestParam(required = false) String sortBy
    ) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all product successful", productService.getAllProductsWithSortBy(pageNo, pageSize, sortBy));
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}

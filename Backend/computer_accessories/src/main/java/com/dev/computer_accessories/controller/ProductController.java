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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@Tag(name = "Product Controller")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Add product", description = "Api create new product")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    public ResponseData<?> saveProduct(@RequestBody ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Product added successfully");
    }


    @Operation(summary = "Delete product", description = "Api delete a product")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{productId}")
    public ResponseData<?> deleteProduct(@PathVariable long productId) {
        productService.deleteProduct(productId);
        return new ResponseData<>(HttpStatus.OK.value(), "Product deleted successfully");
    }

    @Operation(summary = "Update product", description = "Api update a product")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{productId}")
    public ResponseData<?> updateProduct(@Min(value = 1, message = "productId must be greater than 0") @PathVariable long productId, @RequestBody ProductDTO productDTO) {
        productService.updateProduct(productId, productDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Product updated successfully");
    }

    @Operation(summary = "Get product by id", description = "Api get a product by id")
    @GetMapping("/{productId}")
    public ResponseData<?> getProductById(@Min(value = 1, message = "productId must be greater than 0") @PathVariable long productId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get product by id successfully", productService.getProductDetail(productId));
    }


    @Operation(summary = "Get list of products per page", description = "Send a request via this API to get product list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllProduct(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                         @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                         @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all product successful", productService.getAllProductsWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get list of products per page by search", description = "Send a request via this API to get product list by search with pageNo and pageSize")
    @GetMapping("/list-search")
    public ResponseData<?> getAllProductBySearch(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                 @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                 @RequestParam(required = false) String search,
                                                 @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all product successful", productService.searchProduct(pageNo, pageSize, sortBy, search));

    }

    @Operation(summary = "Get list of most promoted products by page", description = "Send a request via this API to get a list of top selling products by pageNo and pageSize")
    @GetMapping("/list-promotion")
    public ResponseData<?> getAllProductPromotion(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                         @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                         @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all product successful", productService.getAllProductsWithSortBy(pageNo, pageSize, sortBy));
    }

    @GetMapping("/test")
    public String testRedis () {
        productService.testRedis();
        return "Complete!";
    }
}

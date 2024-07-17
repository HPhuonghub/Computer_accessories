package com.dev.computer_accessories.controller;

import com.dev.computer_accessories.dto.request.ProductSpecificationDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.ResponseError;
import com.dev.computer_accessories.service.ProductSpecificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product-specification")
@Tag(name="ProductSpecification Controller")
@Slf4j
public class ProductSpecificationController {

    private final ProductSpecificationService productSpecificationService;

    @Operation(summary = "Add productSpecification", description = "Api create new productSpecification")
    @PostMapping("/")
    public ResponseData<?> saveProductSpecification(@RequestBody ProductSpecificationDTO productSpecificationDTO) {
        try {
            productSpecificationService.saveProductSpecification(productSpecificationDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "ProductSpecification added successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Operation(summary = "Delete productSpecificationSpecification", description = "Api delete a productSpecification")
    @DeleteMapping("/{productSpecificationId}")
    public ResponseData<?> deleteProductSpecification(@PathVariable long productSpecificationId) {
        try {
            productSpecificationService.deleteProductSpecification(productSpecificationId);
            return new ResponseData<>(HttpStatus.OK.value(), "ProductSpecification deleted successfully");
        } catch (Exception e) {
            log.error("Error Message={}, Cause={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Update productSpecificationSpecification", description = "Api update a productSpecification")
    @PutMapping("/{productSpecificationId}")
    public ResponseData<?> updateProductSpecification(@Min(value = 1, message = "productSpecificationId must be greater than 0") @PathVariable long productSpecificationId, @RequestBody ProductSpecificationDTO productSpecificationDTO) {
        try {
            productSpecificationService.updateProductSpecification(productSpecificationId, productSpecificationDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "ProductSpecification updated successfully");
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list of productSpecifications per page", description = "Send a request via this API to get productSpecification list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllProductSpecification(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                         @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                         @RequestParam(required = false) String sortBy
    ) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get all productSpecification successful", productSpecificationService.getAllProductSpecificationsWithSortBy(pageNo, pageSize, sortBy));
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get productSpecification by id", description = "Api get a productSpecification by id")
    @GetMapping("/{productSpecificationId}")
    public ResponseData<?> getProductSpecificationById(@Min(value = 1, message = "productSpecificationId must be greater than 0") @PathVariable long productSpecificationId) {
        try {

            return new ResponseData<>(HttpStatus.OK.value(), "Get productSpecification by id successfully",productSpecificationService.getProductSpecification(productSpecificationId));
        } catch (Exception e) {
            log.error("Error Message={}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}

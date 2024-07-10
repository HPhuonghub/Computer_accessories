package com.dev.computer_accessories.controller;


import com.dev.computer_accessories.dto.request.SupplierDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.ResponseError;
import com.dev.computer_accessories.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "Supplier Controller")
@RequestMapping("/api/v1/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "Get supplier by id", description = "Return supplier by id")
    @GetMapping("/{supplierId}")
    public ResponseData<?> getSupplierById(@Valid @PathVariable Long supplierId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get supplier by id successful", supplierService.getSupplier(supplierId));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Add supplier", description = "API create new supplier")
    @PostMapping("/")
    public ResponseData<?> addSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        log.info("Request add supplier = {}", supplierDTO);
        try {
            supplierService.saveSupplier(supplierDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Create a supplier successful");
        } catch (Exception e) {
            log.error("errorMessage = {}",e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Operation(summary = "Update a supplier", description = "API update a supplier")
    @PutMapping("/{supplierId}")
    public ResponseData<?> updateSupplier(@Min(value = 1, message = "SupplierId must be greater than 0") @PathVariable long supplierId, @RequestBody SupplierDTO supplierDTO) {
        try {
            supplierService.updateSupplier(supplierId, supplierDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Update supplier successful");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Delete a supplier", description = "API delete a supplier")
    @DeleteMapping("/{supplierId}")
    public ResponseData<?> deleteSupplier(@Min(1) @PathVariable long supplierId) {
        try {
            supplierService.deleteSupplier(supplierId);
            return new ResponseData<>(HttpStatus.OK.value(), "Delete supplier successful");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list of suppliers per page", description = "Send a request via this API to get supplier list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllSupplierWithSortBy(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all supplier successful", supplierService.getAllSuppliersWithSortBy(pageNo, pageSize, sortBy));
    }
}

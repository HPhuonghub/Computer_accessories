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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "Supplier Controller")
@RequestMapping("/api/v1/supplier")
public class SupplierController {

    private final SupplierService supplierService;

    @Operation(summary = "Get supplier by id", description = "Return supplier by id")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{supplierId}")
    public ResponseData<?> getSupplierById(@Valid @PathVariable Long supplierId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get supplier by id successful", supplierService.getSupplier(supplierId));
    }

    @Operation(summary = "Add supplier", description = "API create new supplier")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/")
    public ResponseData<?> addSupplier(@Valid @RequestBody SupplierDTO supplierDTO) {
        log.info("Request add supplier = {}", supplierDTO);
        supplierService.saveSupplier(supplierDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Create a supplier successful");
    }


    @Operation(summary = "Update a supplier", description = "API update a supplier")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{supplierId}")
    public ResponseData<?> updateSupplier(@Min(value = 1, message = "SupplierId must be greater than 0") @PathVariable long supplierId, @RequestBody SupplierDTO supplierDTO) {
        supplierService.updateSupplier(supplierId, supplierDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Update supplier successful");
    }

    @Operation(summary = "Delete a supplier", description = "API delete a supplier")
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{supplierId}")
    public ResponseData<?> deleteSupplier(@Min(1) @PathVariable long supplierId) {
        supplierService.deleteSupplier(supplierId);
        return new ResponseData<>(HttpStatus.OK.value(), "Delete supplier successful");
    }

    @Operation(summary = "Get list of suppliers per page", description = "Send a request via this API to get supplier list by pageNo and pageSize")
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/lists")
    public ResponseData<?> getAllSupplierWithSortBy(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all supplier successful", supplierService.getAllSuppliersWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get list of suppliers", description = "Send a request via this API to get supplier list")
    @GetMapping("/list")
    public ResponseData<?> getSuppliers() {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all supplier successful", supplierService.getSuppliers());
    }
}

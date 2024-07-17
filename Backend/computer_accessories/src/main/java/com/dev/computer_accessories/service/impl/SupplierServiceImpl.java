package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.SupplierDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.SupplierDetailResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.Supplier;
import com.dev.computer_accessories.repository.SupplierRepository;
import com.dev.computer_accessories.service.SupplierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {
   private final SupplierRepository supplierRepository;

    @Override
    public void saveSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = Supplier.builder()
                .name(supplierDTO.getName())
                .phone(supplierDTO.getPhone())
                .email(supplierDTO.getEmail())
                .address(supplierDTO.getAddress())
                .description(supplierDTO.getDescription())
                .build();

        supplierRepository.save(supplier);
    }

    @Override
    public void updateSupplier(long id, SupplierDTO supplierDTO) {
        Supplier supplier = getSupplierById(id);
        supplier.setName(supplierDTO.getName());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setDescription(supplierDTO.getDescription());

        supplierRepository.save(supplier);
    }

    @Override
    public void deleteSupplier(long id) {
        supplierRepository.deleteById(id);
    }

    @Override
    public PageResponse<?> getAllSuppliersWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if(pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize,Sort.by(sorts));

        Page<Supplier> suppliers = supplierRepository.findAll(pageable);

        List<SupplierDetailResponse> responses = suppliers.stream().map(supplier -> SupplierDetailResponse.builder()
                .id(supplier.getId())
                .name(supplier.getName())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .email(supplier.getEmail())
                .description(supplier.getDescription())
                .build()
        ).toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(suppliers.getTotalPages())
                .items(responses)
                .build();
    }

    @Override
    public SupplierDetailResponse getSupplier(long id) {
        Supplier supplier = getSupplierById(id);
        return SupplierDetailResponse.builder()
                .name(supplier.getName())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .description(supplier.getDescription())
                .email(supplier.getEmail())
                .build();
    }

    @Override
    public List<SupplierDetailResponse> getSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();

        List<SupplierDetailResponse> responses = suppliers.stream().map(supplier -> SupplierDetailResponse.builder()
                        .id(supplier.getId())
                        .name(supplier.getName())
                        .address(supplier.getAddress())
                        .phone(supplier.getPhone())
                        .description(supplier.getDescription())
                        .build())
                .toList();
        return responses;
    }

    private Supplier getSupplierById(long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
    }
}

package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.SupplierDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.SupplierDetailResponse;

public interface SupplierService {
    void saveSupplier(SupplierDTO supplierDTO);

    void updateSupplier(long id, SupplierDTO supplierDTO);

    void deleteSupplier(long id);

    PageResponse<?> getAllSuppliersWithSortBy(int pageNo, int pageSize, String sortBy);

    SupplierDetailResponse getSupplier(long id);


}

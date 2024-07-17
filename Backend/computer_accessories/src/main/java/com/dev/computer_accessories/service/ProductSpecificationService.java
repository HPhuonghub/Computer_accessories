package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.ProductSpecificationDTO;
import com.dev.computer_accessories.dto.response.FeedBackDetailResponse;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.ProductSpecificationDetailResponse;

public interface ProductSpecificationService {
    void saveProductSpecification(ProductSpecificationDTO productSpecificationDTO);

    void updateProductSpecification(long id, ProductSpecificationDTO productSpecificationDTO);

    void deleteProductSpecification(long id);

    PageResponse<?> getAllProductSpecificationsWithSortBy(int pageNo, int pageSize, String sortBy);

    ProductSpecificationDetailResponse getProductSpecification(long id);
}

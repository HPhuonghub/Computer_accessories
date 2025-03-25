package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.ProductDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.ProductDetailResponse;

public interface ProductService {

    void testRedis();

    void saveProduct(ProductDTO productDTO);

    void updateProduct(long id, ProductDTO productDTO);

    void deleteProduct(long id);

    ProductDetailResponse getProductDetail(long id);

    PageResponse<?> getAllProductsWithSortBy(int PageNo, int PageSize, String sortBy);

    PageResponse<?> searchProduct(int pageNo, int pageSize, String sortBy, String keyword);

    PageResponse<?> promotionalProduct(int pageNo, int pageSize, String sortBy, String keyword);
}

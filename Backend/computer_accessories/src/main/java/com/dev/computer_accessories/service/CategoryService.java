package com.dev.computer_accessories.service;

import com.dev.computer_accessories.dto.request.CategoryDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.CategoryDetailResponse;

public interface CategoryService {
    void saveCategory(CategoryDTO categoryDTO);

    void updateCategory(int id, CategoryDTO categoryDTO);

    void deleteCategory(int id);

    PageResponse<?> getAllCategorysWithSortBy(int pageNo, int pageSize, String sortBy);

    CategoryDetailResponse getCategory(int id);
}

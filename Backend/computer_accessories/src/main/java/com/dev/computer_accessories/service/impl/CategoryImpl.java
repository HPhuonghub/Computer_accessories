package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.CategoryDTO;
import com.dev.computer_accessories.dto.response.CategoryDetailResponse;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.exception.AppException;
import com.dev.computer_accessories.exception.ErrorCode;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.Category;
import com.dev.computer_accessories.repository.CategoryRepository;
import com.dev.computer_accessories.service.CategoryService;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public void saveCategory(CategoryDTO categoryDTO) {
        if(existCategory(categoryDTO.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        Category category = Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .build();

        categoryRepository.save(category);

        log.info("Category saved successfully");
    }

    @Override
    public void updateCategory(int id, CategoryDTO categoryDTO) {
        Category category = getCategoryById(id);

        if(existCategory(categoryDTO.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        categoryRepository.save(category);

        log.info("Category updated successfully");
    }

    @Override
    public void deleteCategory(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public PageResponse<?> getAllCategorysWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if(pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                if(matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, "name"));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, "name"));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));

        Page<Category> categorys = categoryRepository.findAll(pageable);

        List<CategoryDetailResponse> res = categorys.stream().map(category -> CategoryDetailResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(categorys.getTotalPages())
                .items(res)
                .build();
    }

    @Override
    public CategoryDetailResponse getCategory(int id) {
        Category category = getCategoryById(id);

        return CategoryDetailResponse.builder()
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public List<CategoryDetailResponse> getCategories() {
        List<Category> categories = categoryRepository.findAll();

        List<CategoryDetailResponse> responses = categories.stream().map(category -> CategoryDetailResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .toList();
        return responses;
    }

    private Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    private boolean existCategory(String name) {
        return categoryRepository.findByName(name).isPresent();
    }
}

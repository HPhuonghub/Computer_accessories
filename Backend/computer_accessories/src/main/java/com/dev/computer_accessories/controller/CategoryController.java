package com.dev.computer_accessories.controller;


import com.dev.computer_accessories.dto.request.CategoryDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
@RestController
@Tag(name = "Category Controller")
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get category by id", description = "Return category by id")
    @GetMapping("/{categoryId}")
    public ResponseData<?> getCategoryById(@Valid @PathVariable int categoryId) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get category by id successful", categoryService.getCategory(categoryId));
    }

    @Operation(summary = "Add category", description = "API create new category")
    @PostMapping("")
    public ResponseData<?> addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        log.info("Request add category = {}", categoryDTO);
        categoryService.saveCategory(categoryDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Create a category successful");
    }


    @Operation(summary = "Update a category", description = "API update a category")
    @PutMapping("/{categoryId}")
    public ResponseData<?> updateCategory(@Min(value = 1, message = "CategoryId must be greater than 0") @PathVariable int categoryId, @RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryId, categoryDTO);
        return new ResponseData<>(HttpStatus.OK.value(), "Update category successful");
    }

    @Operation(summary = "Delete a category", description = "API delete a category")
    @DeleteMapping("/{categoryId}")
    public ResponseData<?> deleteCategory(@Min(1) @PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseData<>(HttpStatus.OK.value(), "Delete category successful");
    }

    @Operation(summary = "Get list of categorys per page", description = "Send a request via this API to get category list by pageNo and pageSize")
    @GetMapping("/lists")
    public ResponseData<?> getAllCategoryWithSortBy(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy
    ) {
        // Lấy Authentication từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            // Lấy thông tin người dùng
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                // Lấy danh sách các vai trò (roles)
                Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

                // In ra các vai trò
                for (GrantedAuthority authority : authorities) {
                    System.out.println("Role: " + authority.getAuthority());
                }
            }
        }
        return new ResponseData<>(HttpStatus.OK.value(), "Get all category successful", categoryService.getAllCategorysWithSortBy(pageNo, pageSize, sortBy));
    }

    @Operation(summary = "Get list of categories", description = "Send a request via this API to get category list")
    @GetMapping("/list")
    public ResponseData<?> getAllCategories() {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all category successful", categoryService.getCategories());
    }
}

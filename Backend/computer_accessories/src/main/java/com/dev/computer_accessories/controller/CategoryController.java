package com.dev.computer_accessories.controller;


import com.dev.computer_accessories.dto.request.CategoryDTO;
import com.dev.computer_accessories.dto.response.ResponseData;
import com.dev.computer_accessories.dto.response.ResponseError;
import com.dev.computer_accessories.service.CategoryService;
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
@Tag(name = "Category Controller")
@RequestMapping("/api/v1/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Get category by id", description = "Return category by id")
    @GetMapping("/{categoryId}")
    public ResponseData<?> getCategoryById(@Valid @PathVariable int categoryId) {
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Get category by id successful", categoryService.getCategory(categoryId));
        } catch (Exception e) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Add category", description = "API create new category")
    @PostMapping("/")
    public ResponseData<?> addCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        log.info("Request add category = {}", categoryDTO);
        try {
            categoryService.saveCategory(categoryDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Create a category successful");
        } catch (Exception e) {
            log.error("errorMessage = {}",e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @Operation(summary = "Update a category", description = "API update a category")
    @PutMapping("/{categoryId}")
    public ResponseData<?> updateCategory(@Min(value = 1, message = "CategoryId must be greater than 0") @PathVariable int categoryId, @RequestBody CategoryDTO categoryDTO) {
        try {
            categoryService.updateCategory(categoryId, categoryDTO);
            return new ResponseData<>(HttpStatus.OK.value(), "Update category successful");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Delete a category", description = "API delete a category")
    @DeleteMapping("/{categoryId}")
    public ResponseData<?> deleteCategory(@Min(1) @PathVariable int categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return new ResponseData<>(HttpStatus.OK.value(), "Delete category successful");
        } catch (Exception e) {
            log.error("errorMessage = {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @Operation(summary = "Get list of categorys per page", description = "Send a request via this API to get category list by pageNo and pageSize")
    @GetMapping("/list")
    public ResponseData<?> getAllCategoryWithSortBy(
            @RequestParam(defaultValue = "0", required = false) int pageNo,
            @Min(1) @RequestParam(defaultValue = "20", required = false) int pageSize,
            @RequestParam(required = false) String sortBy
    ) {
        return new ResponseData<>(HttpStatus.OK.value(), "Get all category successful", categoryService.getAllCategorysWithSortBy(pageNo, pageSize, sortBy));
    }
}

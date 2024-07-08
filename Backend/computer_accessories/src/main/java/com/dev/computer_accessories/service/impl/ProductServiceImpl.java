package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.ProductDTO;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.ProductDetailResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.Category;
import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.Supplier;
import com.dev.computer_accessories.repository.CategoryRepository;
import com.dev.computer_accessories.repository.ProductRepository;
import com.dev.computer_accessories.repository.SupplierRepository;
import com.dev.computer_accessories.service.ProductService;
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
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public void saveProduct(ProductDTO productDTO) {

        Category category = getCategoryByName(productDTO.getCategory().getName());

        Supplier supplier = getSupplierByName(productDTO.getSupplier().getName());

        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .category(category)
                .stock(productDTO.getStock())
                .description(productDTO.getDescription())
                .supplier(supplier)
                .thumbnail(productDTO.getThumbnail())
                .discount(productDTO.getDiscount())
                .build();

        productRepository.save(product);

        log.info("Product saved");
    }

    @Override
    public void updateProduct(long id, ProductDTO productDTO) {
        Product product = getProductById(id);

        Category category = getCategoryByName(productDTO.getCategory().getName());

        Supplier supplier = getSupplierByName(productDTO.getSupplier().getName());

        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setCategory(category);
        product.setStock(productDTO.getStock());
        product.setDescription(productDTO.getDescription());
        product.setSupplier(supplier);
        product.setThumbnail(productDTO.getThumbnail());
        product.setDiscount(productDTO.getDiscount());

        productRepository.save(product);

        log.info("Update user successfully!");
    }


    @Override
    public void deleteProduct(long id) {
        productRepository.deleteById(id);

        log.info("Product deleted!");
    }

    @Override
    public ProductDetailResponse getProductDetail(long id) {
        Product product = getProductById(id);

        return ProductDetailResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .discount(product.getDiscount())
                .stock(product.getStock())
                .thumbnail(product.getThumbnail())
                .build();
    }

    @Override
    public PageResponse<?> getAllProductsWithSortBy(int pageNo, int pageSize, String sortBy) {
        int p = 0;
        if(pageNo > 0) {
            p = pageNo - 1;
        }

        List<Sort.Order> sorts = new ArrayList<>();
        if(StringUtils.hasLength(sortBy)) {
            //name:asc||desc
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sortBy);
            if(matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add((new Sort.Order(Sort.Direction.ASC, matcher.group(1))));
                } else {
                    sorts.add((new Sort.Order(Sort.Direction.DESC, matcher.group(1))));
                }
            }
        }

        Pageable pageable = PageRequest.of(p, pageSize, Sort.by(sorts));

        Page<Product> products = productRepository.findAll(pageable);

        List<ProductDetailResponse> response = products.stream().map(product -> ProductDetailResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .description(product.getDescription())
                        .stock(product.getStock())
                        .thumbnail(product.getThumbnail())
                        .discount(product.getDiscount())
                        .category(product.getCategory())
                        .supplier(product.getSupplier())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(products.getTotalPages())
                .items(response)
                .build();
    }


    private Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    private Category getCategoryByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category name not found"));
    }

    private Supplier getSupplierByName(String name) {
        return supplierRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Supplier name not found"));
    }
}

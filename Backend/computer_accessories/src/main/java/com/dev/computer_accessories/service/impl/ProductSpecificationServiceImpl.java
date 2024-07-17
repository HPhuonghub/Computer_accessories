package com.dev.computer_accessories.service.impl;

import com.dev.computer_accessories.dto.request.ProductSpecificationDTO;
import com.dev.computer_accessories.dto.response.FeedBackDetailResponse;
import com.dev.computer_accessories.dto.response.PageResponse;
import com.dev.computer_accessories.dto.response.ProductSpecificationDetailResponse;
import com.dev.computer_accessories.exception.ResourceNotFoundException;
import com.dev.computer_accessories.model.ProductSpecification;
import com.dev.computer_accessories.model.Product;
import com.dev.computer_accessories.model.User;
import com.dev.computer_accessories.repository.ProductSpecificationRepository;
import com.dev.computer_accessories.repository.ProductRepository;
import com.dev.computer_accessories.repository.UserRepository;
import com.dev.computer_accessories.service.ProductSpecificationService;
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
public class ProductSpecificationServiceImpl implements ProductSpecificationService {
   private final ProductSpecificationRepository productSpecificationRepository;
   private final ProductRepository productRepository;

    @Override
    public void saveProductSpecification(ProductSpecificationDTO productSpecificationDTO) {
        Product product = getProductByName(productSpecificationDTO.getProduct().getName());

        ProductSpecification productSpecification = ProductSpecification.builder()
                .specName(productSpecificationDTO.getSpecName())
                .specValue(productSpecificationDTO.getSpecValue())
                .product(product)
                .build();

        productSpecificationRepository.save(productSpecification);
    }

    @Override
    public void updateProductSpecification(long id, ProductSpecificationDTO productSpecificationDTO) {
        ProductSpecification productSpecification = getProductSpecificationById(id);
        productSpecification.setSpecName(productSpecificationDTO.getSpecName());
        productSpecification.setSpecValue(productSpecificationDTO.getSpecValue());

        productSpecificationRepository.save(productSpecification);
    }

    @Override
    public void deleteProductSpecification(long id) {
        productSpecificationRepository.deleteById(id);
    }

    @Override
    public PageResponse<?> getAllProductSpecificationsWithSortBy(int pageNo, int pageSize, String sortBy) {
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

        Page<ProductSpecification> productSpecifications = productSpecificationRepository.findAll(pageable);

        List<ProductSpecificationDetailResponse> responses = productSpecifications.stream().map(productSpecification -> ProductSpecificationDetailResponse.builder()
                .id(productSpecification.getId())
                .specName(productSpecification.getSpecName())
                .specValue(productSpecification.getSpecValue())
                .product(productSpecification.getProduct())
                .build()
        ).toList();

        return PageResponse.builder()
                .pageNo(p)
                .pageSize(pageSize)
                .totalPage(productSpecifications.getTotalPages())
                .items(responses)
                .build();
    }

    @Override
    public ProductSpecificationDetailResponse getProductSpecification(long id) {
        ProductSpecification productSpecification = getProductSpecificationById(id);
        return ProductSpecificationDetailResponse.builder()
                .specName(productSpecification.getSpecName())
                .specValue(productSpecification.getSpecValue())
                .product(productSpecification.getProduct())
                .build();
    }

    private ProductSpecification getProductSpecificationById(long id) {
        return productSpecificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ProductSpecification not found"));
    }

    private Product getProductByName(String name) {
        return  productRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
}

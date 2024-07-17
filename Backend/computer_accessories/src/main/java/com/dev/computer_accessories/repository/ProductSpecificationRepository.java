package com.dev.computer_accessories.repository;

import com.dev.computer_accessories.model.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
}

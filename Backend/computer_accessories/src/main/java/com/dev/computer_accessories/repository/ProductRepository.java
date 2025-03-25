package com.dev.computer_accessories.repository;

import com.dev.computer_accessories.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p " +
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(p.supplier.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "ORDER BY p.discount DESC")
    Page<Product> promotionalProduct(@Param("keyword") String keyword, Pageable pageable);
}

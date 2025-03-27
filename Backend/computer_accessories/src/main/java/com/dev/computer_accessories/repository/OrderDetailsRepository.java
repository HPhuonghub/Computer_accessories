package com.dev.computer_accessories.repository;

import com.dev.computer_accessories.dto.response.StatisticProductResponse;
import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    // Sử dụng @Query để tìm các OrderDetails theo orderId
    @Query("SELECT od FROM OrderDetails od WHERE od.orders.id = :orderId")
    Optional<List<OrderDetails>> findAllByOrderId(@Param("orderId") Long orderId);

    @Query(value = """
        SELECT p.id AS id, 
               p.name AS name, 
               SUM(od.quantity) AS quantity, 
               SUM(p.price * od.quantity) AS totalPrice
        FROM tbl_order_details AS od
        JOIN tbl_product AS p ON od.product_id = p.id
        WHERE od.created_at BETWEEN :startTime AND :endTime
        GROUP BY p.id, p.name
        """,
            nativeQuery = true)
    Page<Object[]> getStatisticProduct(@Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime,
                                       Pageable pageable);
}

//    @Query(value = """
//            SELECT new com.dev.computer_accessories.dto.response.StatisticProductResponse(
//                p.id,
//                p.name,
//                COALESCE(sum(od.quantity), 0),
//                COALESCE(sum(p.price * od.quantity), 0) AS totalPrice
//            )
//            FROM OrderDetails od
//            JOIN Product p ON od.product.id = p.id
//            WHERE od.createdAt BETWEEN :startTime AND :endTime
//            GROUP BY p.id, p.name
//            ORDER BY totalPrice DESC
//            """, nativeQuery = false)
//    Page<StatisticProductResponse> getStatisticProduct(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);


package com.dev.computer_accessories.repository;

import com.dev.computer_accessories.model.OrderDetails;
import com.dev.computer_accessories.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {

    // Sử dụng @Query để tìm các OrderDetails theo orderId
    @Query("SELECT od FROM OrderDetails od WHERE od.orders.id = :orderId")
    Optional<List<OrderDetails>> findAllByOrderId(@Param("orderId") Long orderId);
}

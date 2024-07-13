package com.dev.computer_accessories.repository;

import com.dev.computer_accessories.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {


    Optional<Orders> findByEmail(String email);
}

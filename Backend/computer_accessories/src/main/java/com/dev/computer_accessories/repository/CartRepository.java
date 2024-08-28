package com.dev.computer_accessories.repository;

import com.dev.computer_accessories.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}

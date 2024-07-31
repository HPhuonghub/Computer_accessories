package com.dev.computer_accessories.repository;


import com.dev.computer_accessories.model.AuthProvider;
import com.dev.computer_accessories.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByfullName(String fullName);

    Optional<User> findByEmailAndProvider(String email, AuthProvider provider);
}

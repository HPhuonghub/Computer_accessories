package com.dev.computer_accessories.repository;

import com.dev.computer_accessories.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = """
            select t from Token t inner join User u\s 
            on t.user.id = u.id\s
            where u.id = :id and (t.expired = false or t.revoked = false )"""
    )
    List<Token> findAllValidTokenByUser(Long id);
}

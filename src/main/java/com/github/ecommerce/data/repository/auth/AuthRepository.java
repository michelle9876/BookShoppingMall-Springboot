package com.github.ecommerce.data.repository.auth;

import com.github.ecommerce.data.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User,Integer> {
    Optional<User>findByEmail(String email);
    boolean existsByEmail(String email);
}

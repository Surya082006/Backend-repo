package com.klu.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrUsername(String email, String username);
}
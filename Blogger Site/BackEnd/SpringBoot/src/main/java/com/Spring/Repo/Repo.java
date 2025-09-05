package com.Spring.Repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Spring.Model.User;

@Repository
public interface Repo extends JpaRepository<User, Long> {
    // Correctly return Optional<User>
    Optional<User> findByUsername(String username);
}

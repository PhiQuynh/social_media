package com.example.project.repository;

import com.example.project.ennity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        User findByUsername(String username);

        boolean existsByUsername(String username);

        User findByTokenPassword(String tokenPassword);
}

package com.example.project.repository;

import com.example.project.ennity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface UserFriendRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
//    List<User> findAllByUsername(Set<String> email);
}

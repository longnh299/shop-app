package com.example.shopbe.repositories;

import com.example.shopbe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByPhoneNumber(String phoneNumber);

    // test query
    @Query("select u from User u where u.fullName = ?1")
    User getUserByNameUsingQuery(String fullName);

}

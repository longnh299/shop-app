package com.example.shopbe.repositories;

import com.example.shopbe.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    // get all order of an user
    List<Order> findByUserId(Long userId);

    int countByUserId(Long userId);

}

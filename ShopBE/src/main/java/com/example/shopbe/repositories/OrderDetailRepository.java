package com.example.shopbe.repositories;

import com.example.shopbe.models.OrderDetail;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    // get all order details by order id
    List<OrderDetail> findByOrderId(Long orderId);
}



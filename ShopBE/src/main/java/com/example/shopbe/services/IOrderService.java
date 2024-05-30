package com.example.shopbe.services;

import com.example.shopbe.dtos.OrderDTO;
import com.example.shopbe.exceptions.DataNotFoundException;
import com.example.shopbe.models.Order;
import com.example.shopbe.responses.OrderResponse;

import java.util.List;

public interface IOrderService {

    OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException;

    OrderResponse getOrder(Long id) throws DataNotFoundException;

    OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;

    void deleteOrder(Long id);

   // List<Order> getAllOrders(Long userId);

    List<Order> findByUserId(Long userId) throws DataNotFoundException;

    int countByUserId(Long userId);

}

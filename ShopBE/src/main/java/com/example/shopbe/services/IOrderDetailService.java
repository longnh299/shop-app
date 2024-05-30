package com.example.shopbe.services;

import com.example.shopbe.dtos.OrderDetailDTO;
import com.example.shopbe.exceptions.DataNotFoundException;
import com.example.shopbe.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {

    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    OrderDetail getOrderDeatil(Long id) throws DataNotFoundException;

    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException;

    void deleteOrderDetail(Long id);

    List<OrderDetail> getOrderDetails(Long orderId);
}
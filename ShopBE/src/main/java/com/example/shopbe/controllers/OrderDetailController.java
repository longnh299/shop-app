package com.example.shopbe.controllers;

import com.example.shopbe.dtos.OrderDetailDTO;
import com.example.shopbe.models.OrderDetail;
import com.example.shopbe.responses.OrderDetailResponse;
import com.example.shopbe.services.IOrderDetailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {

    private IOrderDetailService orderDetailService;

    public OrderDetailController(IOrderDetailService orderDetailService){
        this.orderDetailService = orderDetailService;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO) {
        try {
               OrderDetail newOrderDetail = orderDetailService.createOrderDetail(orderDetailDTO);

            return ResponseEntity.ok(newOrderDetail);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable("id") Long id) {
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDeatil(id);

            OrderDetailResponse orderDetailResponse = OrderDetailResponse.fromOrderDetails(orderDetail);

            return ResponseEntity.ok(orderDetailResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get all order details of order id
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable("orderId") Long orderId) {

        List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(orderId);

        List<OrderDetailResponse> orderDetailResponses = orderDetails.stream()
                .map(orderDetail -> OrderDetailResponse.fromOrderDetails(orderDetail))
                .toList();

        return ResponseEntity.ok(orderDetailResponses);

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@PathVariable("id") Long id, @RequestBody OrderDetailDTO orderDetailData) {
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailData);

            return ResponseEntity.ok(orderDetail);

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrderDetail(@PathVariable("id") Long id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok().body("Delete Order detail has id: " + id + " successfully!");
    }
}


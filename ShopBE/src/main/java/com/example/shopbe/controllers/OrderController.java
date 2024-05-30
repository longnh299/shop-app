package com.example.shopbe.controllers;

import com.example.shopbe.dtos.OrderDTO;
import com.example.shopbe.models.Order;
import com.example.shopbe.responses.OrderResponse;
import com.example.shopbe.services.OrderService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

    private OrderService orderService;

    private ModelMapper modelMapper;

    @Autowired
    public OrderController(final OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult) {

        try {
            if(bindingResult.hasErrors()){
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            OrderResponse orderResponse = orderService.createOrder(orderDTO);

            return ResponseEntity.ok(orderResponse);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            List<Order> orders = orderService.findByUserId(userId);

            Type listType = new TypeToken<List<OrderResponse>>(){}.getType();
            List<OrderResponse> orderResponses = modelMapper.map(orders,listType);

            return ResponseEntity.ok(orderResponses);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable("id") Long id) {
        try {

            OrderResponse orderResponse = orderService.getOrder(id);

            return ResponseEntity.ok(orderResponse);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable("id") Long id,@Valid @RequestBody OrderDTO orderDTO, BindingResult bindingResult){
        try {
            if(bindingResult.hasErrors()){
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            OrderResponse updatedOrder = orderService.updateOrder(id, orderDTO);

            return ResponseEntity.ok(updatedOrder);

        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Long id){
        // soft delete => update active feild
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Delete order " + id + " successfully!");
    }

    @GetMapping("/count/user/{id}")
    public ResponseEntity<?> getOrderCountByUserId(@PathVariable("id") Long id){
        return ResponseEntity.ok(orderService.countByUserId(id));
    }
}

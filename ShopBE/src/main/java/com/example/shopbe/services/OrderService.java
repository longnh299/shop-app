package com.example.shopbe.services;

import com.example.shopbe.dtos.OrderDTO;
import com.example.shopbe.exceptions.DataNotFoundException;
import com.example.shopbe.models.Order;
import com.example.shopbe.models.Status;
import com.example.shopbe.models.User;
import com.example.shopbe.repositories.OrderRepository;
import com.example.shopbe.repositories.UserRepository;
import com.example.shopbe.responses.OrderResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements IOrderService{

    private OrderRepository orderRepository;

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) throws DataNotFoundException {

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User has ID: " + orderDTO.getUserId() + " not found"));

        //using model mapper to map orderDTO to order, ModelMapper use to map same field in two object
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId)); // skip map order id

        Order order = new Order();

        modelMapper.map(orderDTO, order);

        order.setUser(user);

        order.setOrderDate(LocalDateTime.now()); // get current time

        order.setStatus(Status.pending);

        // check shipping date must be after than order date
        LocalDateTime shippingDate = orderDTO.getShippingDate() == null ? LocalDateTime.now() : orderDTO.getShippingDate();

        if(shippingDate.isBefore(order.getOrderDate())) {
            throw new DataNotFoundException("Shipping Date must be after order date");
        }

        order.setShippingDate(shippingDate);

        order.setActive(true);

        orderRepository.save(order);

        OrderResponse orderResponse = new OrderResponse();
        modelMapper.typeMap(Order.class, OrderResponse.class);
        modelMapper.map(order, orderResponse);
        orderResponse.setUserId(order.getUser().getId());
        return orderResponse;

//        modelMapper.typeMap(Order.class, OrderResponse.class);
//        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(Long id) throws DataNotFoundException {

        Order order = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Order has id: " + id + " not found!"));

        OrderResponse orderResponse = new OrderResponse();
        modelMapper.typeMap(Order.class, OrderResponse.class);
        modelMapper.map(order, orderResponse);
        orderResponse.setUserId(order.getUser().getId());

        return orderResponse;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {

        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User has ID: " + orderDTO.getUserId() + " not found"));

        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Order has id: " + id + " not found!"));

        //using model mapper to map orderDTO to order, ModelMapper use to map same field in two object
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId)); // skip map order id

        modelMapper.map(orderDTO, existingOrder);

        existingOrder.setUser(user);

        orderRepository.save(existingOrder);

        OrderResponse orderResponse = new OrderResponse();
        modelMapper.typeMap(Order.class, OrderResponse.class);
        modelMapper.map(existingOrder, orderResponse);
        orderResponse.setUserId(existingOrder.getUser().getId());
        return orderResponse;

    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);

        if (order != null){
            order.setActive(false);
        }

        orderRepository.save(order);
    }

//    @Override
//    public List<Order> getAllOrders(Long userId) {
//        return
//    }

    @Override
    public List<Order> findByUserId(Long userId) throws DataNotFoundException {

        return orderRepository.findByUserId(userId);
    }

    @Override
    public int countByUserId(Long userId) {
        return orderRepository.countByUserId(userId);
    }


}

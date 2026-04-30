package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Order;
import com.diego.mi_primer_api.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findByClient(Long clientId) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Order save(Order order) {
        if(orderRepository.existsByOrderNumber(order.getOrderNumber())) {
            throw new RuntimeException("Ya existe un pedido con el número: " +  order.getOrderNumber());
        }
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Optional<Order> update(Long id, Order order) {
        return orderRepository.findById(id).map(orderDB -> {
            orderDB.setOrderNumber(order.getOrderNumber());
            orderDB.setOrderDate(order.getOrderDate());
            orderDB.setOrderStatus(order.getOrderStatus());
            orderDB.setTrackingNumber(order.getTrackingNumber());
            return orderRepository.save(orderDB);
        });
    }

    @Override
    @Transactional
    public Optional<Order> delete(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        orderOptional.ifPresent(orderDb -> orderRepository.delete(orderDb));

        return orderOptional;
    }
}

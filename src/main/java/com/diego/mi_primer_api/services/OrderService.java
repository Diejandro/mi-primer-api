package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    List<Order> findAll();
    Optional<Order> findById(Long id);
    List<Order> findByClient(Long clientId);
    Order save(Order order);
    Optional<Order> update(Long id, Order order);
    Optional<Order> delete(Long id);
}

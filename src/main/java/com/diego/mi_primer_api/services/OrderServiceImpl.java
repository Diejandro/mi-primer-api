package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.entities.Order;
import com.diego.mi_primer_api.entities.Product;
import com.diego.mi_primer_api.repositories.ClientRepository;
import com.diego.mi_primer_api.repositories.OrderRepository;
import com.diego.mi_primer_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

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
    public List<Order> findByClient(Long clientId) {
        return orderRepository.findByClientId(clientId) ;
    }


    //---------------------

    @Override
    @Transactional
    public Order save(Order order) {
        if( order.getClient() == null || order.getClient().getId() == null){
            throw new IllegalArgumentException("Client id is null");
        }

        Long clientId = order.getClient().getId();
        Optional<Client> clientOptional = clientRepository.findById(clientId);

        if(clientOptional.isEmpty()){
            throw new RuntimeException("Client not found");
        }
        order.setClient(clientOptional.get());

        if( order.getProducts().isEmpty()){
            throw new IllegalArgumentException("Product is empty");
        }

        order.setProducts(order.getProducts().stream()
                .map(p ->
                        productRepository.findById(p.getId()).orElseThrow(() ->
                                new RuntimeException("Product not found by id: " + p.getId())))
                .toList());

        order.setOrderDate(LocalDateTime.now());

        order.setOrderStatus("PEDIDO");

        String numOrder;
        int intentos = 0;

        do{
            if(intentos > 5){throw new RuntimeException("No se pudo generar un número de pedido único tras 5 intentos");}
            numOrder = UUID.randomUUID().toString().substring(24);
            intentos++;
        }while(orderRepository.existsByOrderNumber(numOrder));
        order.setOrderNumber(numOrder);

        order.setOrderNumber(numOrder);
        order.setTrackingNumber(null);

        return orderRepository.save(order);

    }


    //---------------------

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

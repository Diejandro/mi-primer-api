package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.entities.Order;
import com.diego.mi_primer_api.repositories.ClientRepository;
import com.diego.mi_primer_api.repositories.OrderRepository;
import com.diego.mi_primer_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
            if(intentos > 5){throw new RuntimeException("order number could not be generated after 5 times");}
            numOrder = UUID.randomUUID().toString().substring(24);
            intentos++;
        }while(orderRepository.existsByOrderNumber(numOrder));
        order.setOrderNumber(numOrder);

        order.setOrderNumber(numOrder);
        order.setTrackingNumber(null);

        return orderRepository.save(order);

    }

    @Override
    @Transactional
    public Optional<Order> update(Long id, Order orderDetails) {
        return orderRepository.findById(id).map(orderDb -> {

            String newTrackingNumber = orderDetails.getTrackingNumber();
            String currentTrackingNumber = orderDb.getTrackingNumber();

            if (newTrackingNumber != null && !newTrackingNumber.equals(currentTrackingNumber)) {
                if (orderRepository.existsByTrackingNumber(newTrackingNumber)) {
                    throw new RuntimeException("Tracking is already assigned to another order");
                }
                orderDb.setTrackingNumber(newTrackingNumber);
            }

            if (orderDetails.getOrderStatus() != null) {
                orderDb.setOrderStatus(orderDetails.getOrderStatus());
            }

            return orderRepository.save(orderDb);
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

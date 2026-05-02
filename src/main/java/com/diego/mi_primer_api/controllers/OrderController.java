package com.diego.mi_primer_api.controllers;

import com.diego.mi_primer_api.entities.Order;
import com.diego.mi_primer_api.services.OrderService;
import com.diego.mi_primer_api.utils.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> listAllOrders() {
        List<Order> listOrders = orderService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewOrderById(@PathVariable Long id) {
        Optional<Order> orderOptinal = orderService.findById(id);
        if (orderOptinal.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(orderOptinal.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/client/{client_id}")
    public ResponseEntity<?> viewOrdersByClientId(@PathVariable Long client_id) {
        List<Order> orderList = orderService.findByClient(client_id);

        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order order, BindingResult result) {
        if (result.hasErrors()) return ValidationUtils.validationError(result);

        try{
            Order newOrder = orderService.save(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(newOrder);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody Order order, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) return ValidationUtils.validationError(result);

        try{
            return orderService.update(id, order)
                    .map(o -> ResponseEntity.status(HttpStatus.OK).body(o))
                    .orElse(ResponseEntity.notFound().build());
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {

        return orderService.delete(id)
                .map(order -> ResponseEntity.status(HttpStatus.NO_CONTENT).build())
                .orElse(ResponseEntity.notFound().build());
    }
}

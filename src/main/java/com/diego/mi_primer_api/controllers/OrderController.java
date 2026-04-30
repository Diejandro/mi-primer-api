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

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order order, BindingResult result) {
        if (result.hasErrors()) return ValidationUtils.validationError(result);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.save(order));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody Order order, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) ValidationUtils.validationError(result);

        Optional<Order> orderOptional = orderService.update(id,  order);

        if (orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(orderOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        Optional<Order> orderOptional = orderService.delete(id);

        if (orderOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(orderOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}

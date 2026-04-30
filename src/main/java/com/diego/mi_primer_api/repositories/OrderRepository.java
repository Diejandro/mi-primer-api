package com.diego.mi_primer_api.repositories;

import com.diego.mi_primer_api.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClientId(Long clientId);
    boolean existsByOrderNumber(String orderNumber);

}

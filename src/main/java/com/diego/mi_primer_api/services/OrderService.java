package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;
}

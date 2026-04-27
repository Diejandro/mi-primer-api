package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Product;
import com.diego.mi_primer_api.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Optional<Product> update(Long id, Product product) {
        return productRepository.findById(id).map(productDb -> {
            productDb.setProductName(product.getProductName());
            productDb.setProductDescription(product.getProductDescription());
            productDb.setProductPrice(product.getProductPrice());
            productDb.setProductSKU(product.getProductSKU());
            return productRepository.save(productDb);
        });
    }

    @Override
    @Transactional
    public Optional<Product> delete(Long id) {

        Optional<Product> productOptional = productRepository.findById(id);
        productOptional.ifPresent(productDb -> productRepository.delete(productDb));

        return productOptional;
    }
}

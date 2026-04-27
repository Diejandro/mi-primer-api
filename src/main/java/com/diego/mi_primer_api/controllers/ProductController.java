package com.diego.mi_primer_api.controllers;

import com.diego.mi_primer_api.entities.Product;
import com.diego.mi_primer_api.services.ProductService;
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
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<?> listAllProducts(){

        List<Product> listProducts = productService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listProducts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewProductById(@PathVariable Long id){
        Optional<Product> productOptional = productService.findById(id);
        if(productOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(productOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product, BindingResult result){
        if(result.hasErrors()) return ValidationUtils.validationError(result);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.save(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody Product product, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()) return ValidationUtils.validationError(result);

        Optional<Product> productOptional = productService.update(id, product);
        if(productOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(productOptional.orElseThrow());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){

        Optional<Product> productOptional = productService.delete(id);
        if(productOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

}

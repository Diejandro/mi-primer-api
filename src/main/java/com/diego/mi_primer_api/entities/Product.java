package com.diego.mi_primer_api.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productUsk;
    private String productName;
    private String productDescription;
    private Double productPrice;

    @ManyToMany
    private List<Order> orders;

    public Product() {
        this.orders = new ArrayList<>();
    }

    public Product(String productUsk, String productName, String productDescription, Double productPrice) {
        this();
        this.productUsk = productUsk;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductUsk() {
        return productUsk;
    }

    public void setProductUsk(String productUsk) {
        this.productUsk = productUsk;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productUsk, product.productUsk);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productUsk);
    }
}

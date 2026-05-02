package com.diego.mi_primer_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "customer_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String orderNumber;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @NotBlank
    @Column(name = "order_status", nullable = false)
    private String orderStatus;
    
    @Column(name = "tracking_number",unique = true)
    private String trackingNumber;


    @ManyToMany
    @NotEmpty(message = "{NotEmpty.order.product}")
    @JoinTable(
            name = "rel_orders_products",
            joinColumns = @JoinColumn(name="order_id"),
            inverseJoinColumns = @JoinColumn(name="product_id")
    )
    private List<Product> products;

    @NotNull(message = "{NotNull.order.client}")
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Order() {
        this.products = new ArrayList<>();
    }

    public Order(LocalDateTime orderDate, String trackingNumber, String orderStatus, String orderNumber) {
        this();
        this.orderDate = orderDate;
        this.trackingNumber = trackingNumber;
        this.orderStatus = orderStatus;
        this.orderNumber = orderNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderNumber, order.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderNumber);
    }
}

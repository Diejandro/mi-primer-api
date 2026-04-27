package com.diego.mi_primer_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{NotBlank.product.productName}")
    @Size(min = 4, max = 50, message = "{Size.product.productName}")
    @Column(name = "product_name")
    private String productName;

    @NotBlank(message = "{NotBlank.product.productDescription}")
    @Column(name = "product_description",columnDefinition = "TEXT")
    private String productDescription;

    @NotNull(message = "{NotNull.product.productPrice}")
    @DecimalMin(value = "0.01", message = "{DecimalMin.product.productPrice}")
    @Column(name = "product_price", precision = 12, scale = 2)
    private BigDecimal productPrice;

    @NotBlank(message = "{NoBlank.product.productSKU}")
    @Pattern(regexp = "^[A-Z]{3}-[A-Z0-9]{4,5}-[0-9]{2}$", message = "{Pattern.product.productSKU}")
    @Column(name = "sku_code", unique = true)
    private String productSKU;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;

    public Product() {
        this.orders = new ArrayList<>();
    }

    public Product(String productSKU, String productName, String productDescription, BigDecimal productPrice) {
        this();
        this.productSKU = productSKU;
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

    public String getProductSKU() {
        return productSKU;
    }

    public void setProductSKU(String productSKU) {
        this.productSKU = productSKU;
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

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
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
        return Objects.equals(productSKU, product.productSKU);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productSKU);
    }
}

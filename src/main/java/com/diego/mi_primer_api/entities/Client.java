package com.diego.mi_primer_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{NotBlank.client.name}")
    @Size(min = 3, max = 50, message = "{Size.client.name}")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "{NotBlank.client.email}")
    @Email(message = "{Email.client.email}")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "{NotBlank.client.clientNumId}")
    @Pattern(regexp = "^[0-9]{8}[a-zA-Z]$", message = "{Pattern.client.clientNumId}")
    @Column(name = "client_num_id", unique = true, nullable = false)
    private String clientNumId;

    @OneToMany(mappedBy="client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    public Client() {
        this.orders = new ArrayList<>();
    }

    public Client(String name, String email, String clientNumId) {
        this();
        this.name = name;
        this.email = email;
        this.clientNumId = clientNumId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getClientNumId() {
        return clientNumId;
    }

    public void setClientNumId(String clientNumId) {
        this.clientNumId = clientNumId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(clientNumId, client.clientNumId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(clientNumId);
    }
}

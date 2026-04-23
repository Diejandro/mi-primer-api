package com.diego.mi_primer_api.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String clientNumId;

    @OneToMany(mappedBy="clients")
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

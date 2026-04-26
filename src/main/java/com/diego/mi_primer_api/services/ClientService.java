package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<Client> findAll();
    Optional<Client> findById(Long id);
    Client save(Client client);
    Optional<Client> update(Long id, Client client);
    Optional<Client> delete(Long id);
}

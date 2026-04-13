package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public Client findById(Long id){
        return clientRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public Client save(Client client){
        return clientRepository.save(client);
    }

    public void delete(Long id){
        clientRepository.deleteById(id);
    }

    public Client update(Long id, Client clientDetails) {
        Client client = findById(id);
        client.setName(clientDetails.getName());
        client.setEmail(clientDetails.getEmail());
        return clientRepository.save(client);
    }
}

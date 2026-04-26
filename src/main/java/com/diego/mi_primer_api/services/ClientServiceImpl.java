package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;


    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    @Transactional
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public Optional<Client> update(Long id, Client client) {
        return clientRepository.findById(id).map(clientDb -> {
            clientDb.setName(client.getName());
            clientDb.setEmail(client.getEmail());
            clientDb.setClientNumId(client.getClientNumId());
            return clientRepository.save(clientDb);
        });
    }

    @Override
    @Transactional
    public Optional<Client> delete(Long id) {

        Optional <Client> clientOptional = clientRepository.findById(id);
        clientOptional.ifPresent(clientDb -> clientRepository.delete(clientDb));

        return clientOptional;
    }
}

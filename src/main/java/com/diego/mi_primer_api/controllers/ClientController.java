package com.diego.mi_primer_api.controllers;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.repositories.ClientRepository;
import com.diego.mi_primer_api.services.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public List<Client> getAllClients(){
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client getClientById(@PathVariable Long id){
        return clientService.findById(id);
    }

    @PostMapping
    public Client createClient(@Valid @RequestBody Client client){
        return clientService.save(client);
    }

    @PutMapping("/{id}")
    public Client updateClient(@PathVariable Long id,@Valid @RequestBody Client clientDetails){

        Client client = clientService.findById(id);

        client.setName(clientDetails.getName());
        client.setEmail(clientDetails.getEmail());

        return clientService.save(client);
    }

    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable Long id){

        clientService.delete(id);
        return "Cliente con el ID: " + id + " ha sido eliminado correctamente";
    }

}

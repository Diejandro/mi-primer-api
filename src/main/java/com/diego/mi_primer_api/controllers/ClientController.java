package com.diego.mi_primer_api.controllers;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.services.ClientService;
import com.diego.mi_primer_api.services.ClientServiceImpl;
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
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public ResponseEntity<?> listAllClients(){
        List<Client> clientList = clientService.findAll();

        return ResponseEntity.status(HttpStatus.OK).body(clientList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewClientsById(@PathVariable Long id){
        Optional<Client> clientOptional = clientService.findById(id);
        if(clientOptional.isPresent()){
            return ResponseEntity.ok(clientOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody Client client, BindingResult result){
        if(result.hasErrors()) return ValidationUtils.validationError(result);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.save(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()) return ValidationUtils.validationError(result);

        Optional<Client> clientOptional = clientService.update(id, client);
        if(clientOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(clientOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id){
        Optional<Client> clientOptional = clientService.delete(id);

        if(clientOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(clientOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }
}

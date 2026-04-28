package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client("nameTest", "email@test.com", "45323456R");
        client.setId(1L);
    }

    @Test
    void findById_shouldReturnClient_WhenIdExists() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.findById(1L);

        assertTrue(result.isPresent(), "El resultado debe contener un cliente");
        assertEquals("nameTest", result.get().getName(), "El nombre debe coincidir con el del setup");
        assertEquals("45323456R", result.get().getClientNumId(), "El DNI debería coincidir con el del setup");

        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void findById_shouldReturnEmpty_WhenIdDoesntExists() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.findById(1L);

        assertFalse(result.isPresent(), "El resultado debe estar vacío");

        verify(clientRepository).findById(1L);

    }

    @Test
    void save_shouldSaveClient_whenIdNotExists() {
        client.setId(null);

        Client clientGuardadoEnDB = new Client("nameTest", "email@test.com", "45323456R");
        clientGuardadoEnDB.setId(1L);

        when(clientRepository.save(any(Client.class))).thenReturn(clientGuardadoEnDB);

        Client savedClient = clientService.save(client);

        assertNotNull(savedClient);
        assertEquals(1L, savedClient.getId());
        assertEquals("nameTest", savedClient.getName());

        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void updateClient_shouldReturnUpdatedClient(){
        Long id = 1L;
        Client datosNuevos = new Client("nameTestActualizado", "email1@test1.com", "12345678R");

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(datosNuevos);

        Optional<Client> result = clientService.update(id, datosNuevos);

        assertTrue(result.isPresent());
        assertEquals("nameTestActualizado", result.get().getName());

        verify(clientRepository).findById(id);
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void update_shouldReturnEmpty_WhenIdDoesntExists() {
        Long id = 1L;
        Client clientConCambios = new Client("nameTest", "email@test.com", "45323456R");

        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.update(id, clientConCambios);

        assertFalse(result.isPresent(), "El resultado debe ser un Optional vacío");

        verify(clientRepository).findById(id);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void deleteById_shouldDeleteClient_WhenIdExists() {
        Long id = 1L;

        when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.delete(id);

        assertTrue(result.isPresent(), "El resultado debe contener el cliente que se va a borrar");
        assertEquals("nameTest", result.get().getName());

        verify(clientRepository).findById(id);
        verify(clientRepository).delete(client);
    }

    @Test
    void deleteById_shouldNotDeleteClient_WhenIdDoesntExists() {

        Long id = 1L;
        when(clientRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Client> result = clientService.delete(id);

        assertFalse(result.isPresent(), "El resultado debe ser un Optional vacío");

        verify(clientRepository).findById(id);
        verify(clientRepository, never()).delete(any(Client.class));
    }
}

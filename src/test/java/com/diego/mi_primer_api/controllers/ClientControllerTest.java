package com.diego.mi_primer_api.controllers;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private Client client;
    private Client client2;

    @BeforeEach
    public void setup() {
        client = new Client("nameTest", "email@test.com", "12345678T");
        client.setId(1L);
        client2 = new Client("nameTest2", "email@test.com2", "12345678A");
        client2.setId(1L);
    }

    @Test
    void findById_shouldReturnClient_whenIdExists() {
        when(clientService.findById(1L)).thenReturn(Optional.of(client));

        ResponseEntity<?> response = clientController.viewClientsById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Client body = (Client) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getName()).isEqualTo("nameTest");
        assertThat(body.getEmail()).isEqualTo("email@test.com");

        verify(clientService).findById(1L);
    }

    @Test
    void findById_shouldReturn404_whenIdNotFound() {
        when(clientService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.viewClientsById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(clientService).findById(99L);
    }

    @Test
    void findAll_shouldReturnClients_whenIdExists() {
        when(clientService.findAll()).thenReturn(Arrays.asList(client, client2));

        ResponseEntity<?> response = clientController.listAllClients();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Client> body = (List<Client>) response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.size()).isEqualTo(2);
        assertThat(body).contains(client, client2);

        verify(clientService).findAll();
    }

    @Test
    void createClient_shouldReturnCreatedClient_WhenClientIsValid(){
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        client.setId(null);

        Client clientGuardadoEnDB = new Client("nameTest", "email@test.com", "45323456R");
        clientGuardadoEnDB.setId(1L);

        when(clientService.save(any(Client.class))).thenReturn(clientGuardadoEnDB);

        ResponseEntity<?> response = clientController.createClient(client, result);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Client body = (Client) response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getName()).isEqualTo("nameTest");
        assertThat(body.getEmail()).isEqualTo("email@test.com");
        assertThat(body.getClientNumId()).isEqualTo("45323456R");

        verify(clientService).save(any(Client.class));
    }

    @Test
    void createClient_shouldReturnBadRequest_WhenBindingResultHasErrors(){
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = clientController.createClient(client, result);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(clientService);
    }

    @Test
    void updateClient_shouldReturnUpdatedClient_WhenExistsAndIsValid(){
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        Long id = 1L;
        Client clientUpdated = new Client("nameTestActualizado", "emailActualizado@test.com", "12345678U");
        clientUpdated.setId(id);

        when(clientService.update(eq(id), any(Client.class))).thenReturn(Optional.of(clientUpdated));

        ResponseEntity<?> response = clientController.updateClient(client, result, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Client body = (Client) response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getName()).isEqualTo("nameTestActualizado");

        verify(clientService).update(eq(id), any(Client.class));
    }

    @Test
    void updateClient_shouldReturnBadRequest_WhenBindingResultHasErrors(){
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = clientController.updateClient(client, result, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(clientService);
    }

    @Test
    void updateClient_shouldReturnNotFound_WhenIdNotFound(){
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        when(clientService.update(eq(1L), any(Client.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = clientController.updateClient(client, result, 1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(clientService).update(eq(1L), any(Client.class));

    }

    @Test
    void deleteClient_shouldReturnDeletedClient_WhenExistsAndIsValid(){

        Long id = 1L;

        when(clientService.delete(id)).thenReturn(Optional.of(client));

        ResponseEntity<?> responde = clientController.deleteClient(id);

        assertThat(responde.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(clientService).delete(id);
    }

    @Test
    void deleteClient_shouldReturnNotFound_WhenIdDoesNotExist(){

        when(clientService.delete(1L)).thenReturn(Optional.empty());
        ResponseEntity<?> response = clientController.deleteClient(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(clientService).delete(1L);
    }
}

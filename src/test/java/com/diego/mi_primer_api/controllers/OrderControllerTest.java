package com.diego.mi_primer_api.controllers;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.entities.Order;
import com.diego.mi_primer_api.entities.Product;
import com.diego.mi_primer_api.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;

    private Client client;
    private Product product;
    private Product product2;
    private Order order;

    @BeforeEach
    void setUp() {
        client = new Client("ClientTest", "Client@exampleTest.com", "12345678T");
        client.setId(1L);

        product = new Product("LPT-1234-10", "productTest", "descriptionTest", BigDecimal.valueOf(1000.99));
        product.setId(1L);
        product2 = new Product("TPL-4321-01", "productTestSave", "descriptionTestSave", BigDecimal.valueOf(2000.99));
        product2.setId(2L);

        order = new Order(LocalDateTime.now(), "123456789012", "PEDIDO", "210987654321");
        order.setId(1L);
        order.setClient(client);
        order.setProducts(Arrays.asList(product,  product2));
    }

    @Test
    void findById_shouldReturnOrder_WhenIdExists() {
        when(orderService.findById(1L)).thenReturn(Optional.of(order));

        ResponseEntity<?> response = orderController.viewOrderById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Order body = (Order) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getClient()).isEqualTo(client);
        assertThat(body.getProducts()).isEqualTo(Arrays.asList(product, product2));

        verify(orderService).findById(1L);
    }

    @Test
    void findById_shouldReturnEmpty_WhenIdDoesNotExist() {
        when(orderService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = orderController.viewOrderById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(orderService).findById(99L);
    }

    @Test
    void findByClientId_shouldReturnOrder_WhenClientIdExists() {
        when(orderService.findByClient(client.getId())).thenReturn(List.of(order));

        ResponseEntity<?> response = orderController.viewOrdersByClientId(client.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Order> body = (List<Order>) response.getBody();

        assertThat(body).isNotNull();

        verify(orderService).findByClient(client.getId());

    }

    @Test
    void findByClientId_shouldReturnEmptyList_WhenClientIdDoesNotExist() {
        when(orderService.findByClient(99L)).thenReturn(List.of());

        ResponseEntity<?> response = orderController.viewOrdersByClientId(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(orderService).findByClient(99L);
    }

    @Test
    void createOrder_shouldReturnCreatedOrder_WhenOrderIsValid() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        order.setId(null);

        Order orderDb = new Order(LocalDateTime.now(), "123456789012", "PEDIDO", "210987654321");
        orderDb.setClient(client);
        orderDb.setProducts(Arrays.asList(product,  product2));
        orderDb.setId(1L);

        when(orderService.save(any(Order.class))).thenReturn(orderDb);

        ResponseEntity<?> response = orderController.createOrder(order, result);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Order body = (Order) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getOrderNumber()).isEqualTo("210987654321");
        assertThat(body.getTrackingNumber()).isEqualTo("123456789012");

        verify(orderService).save(any(Order.class));
    }

    @Test
    void createOrder_shouldReturnBadRequest_WhenBindingResultHasErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = orderController.createOrder(order, result);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(orderService);
    }

    @Test
    void updateOrder_shouldReturnUpdatedOrder_WhenOrderIsValid() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        Order orderUpdated = new Order(LocalDateTime.now(), "123456938475", "EN TRÁNSITO", "210987654321");

        when(orderService.update(eq(1L), any(Order.class))).thenReturn(Optional.of(orderUpdated));

        ResponseEntity<?> response = orderController.updateOrder(orderUpdated, result, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Order body = (Order) response.getBody();

        assertThat(body).isNotNull();

        verify(orderService).update(eq(1L), any(Order.class));

    }

    @Test
    void updateOrder_shouldReturnBadRequest_WhenBindingResultHasErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);
        ResponseEntity<?> response = orderController.updateOrder(order, result, 1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(orderService);
    }

    @Test
    void updateOrder_shouldReturnNotFound_WhenOrderIdNotFound() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        when(orderService.update(eq(1L), any(Order.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = orderController.updateOrder(order, result, 1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(orderService).update(eq(1L), any(Order.class));
    }

    @Test
    void deleteOrder_shouldReturnDeletedOrder_WhenOrderIsValid() {

        Long orderId = 1L;

        when(orderService.delete(orderId)).thenReturn(Optional.of(order));

        ResponseEntity<?> response = orderController.deleteOrder(orderId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(orderService).delete(orderId);
    }

    @Test
    void deleteOrder_shouldReturnNotFound_WhenOrderIdNotFound() {

        when(orderService.delete(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = orderController.deleteOrder(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(orderService).delete(1L);
    }
}

package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Client;
import com.diego.mi_primer_api.entities.Order;
import com.diego.mi_primer_api.entities.Product;
import com.diego.mi_primer_api.repositories.ClientRepository;
import com.diego.mi_primer_api.repositories.OrderRepository;
import com.diego.mi_primer_api.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

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
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.findById(1L);

        assertTrue(result.isPresent(), "El resultado debe contener el pedido");
        assertEquals("210987654321", result.get().getOrderNumber(), "El order Number debe coincidir con el del setup");
        assertEquals("123456789012", result.get().getTrackingNumber(), "El tracking number debe coincidir con el del setup");

        verify(orderRepository).findById(1L);
    }

    @Test
    void findById_shouldNotReturnOrder_WhenIdDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.findById(1L);

        assertFalse(result.isPresent(), "El resultado debe estar vacío");

        verify(orderRepository).findById(1L);
    }

    @Test
    void findByClient_shouldReturnOrders_WhenClientExists() {
        when(orderRepository.findByClientId(1L)).thenReturn(List.of(order));
        
        List<Order> result = orderService.findByClient(1L);

        assertFalse(result.isEmpty(), "La lista debería estás vacía para un cliente sin registro");
        assertEquals(1, result.size(), "El resultado debe contener 1 pedido");

        verify(orderRepository).findByClientId(1L);
    }

    @Test
    void findByClient_shouldNotReturnOrders_WhenClientDoesNotExist() {
        when(orderRepository.findByClientId(1L)).thenReturn(Collections.emptyList());

        List<Order> result = orderService.findByClient(1L);

        assertTrue(result.isEmpty(), "El resultado debe contener pedido/os");
        verify(orderRepository).findByClientId(1L);
    }

    @Test
    void save_Success() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
        when(orderRepository.existsByOrderNumber(anyString())).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.save(order);

        assertNotNull(result.getOrderNumber(), "El orderNumber debe haberse generado");
        assertEquals(12, result.getOrderNumber().length(), "El substring(24) de un UUID tiene 12 caracteres");
        assertEquals("PEDIDO", result.getOrderStatus());
        assertNull(result.getTrackingNumber(), "El tracking debe ser null por diseño en el save");

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void save_shouldThrowException_WhenClientDoesNotExist() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.save(order);
        });

        assertEquals("Client not found", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void save_shouldThrowException_WhenProductDoesNotExist() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.save(order);
        });

        assertEquals("Product not found by id: 2", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void update_Success() {

        Order orderDetails = new Order();
        orderDetails.setTrackingNumber("TRACK-OK-123");
        orderDetails.setOrderStatus("ENVIADO");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.existsByTrackingNumber("TRACK-OK-123")).thenReturn(false);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Optional<Order> result = orderService.update(1L, orderDetails);

        assertTrue(result.isPresent());
        assertEquals("ENVIADO", result.get().getOrderStatus());
        assertEquals("TRACK-OK-123", result.get().getTrackingNumber());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void update_shouldThrowException_WhenTrackingAlreadyExists() {

        Order orderDetails = new Order();
        orderDetails.setTrackingNumber("TRACK-DUPLICADO");

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.existsByTrackingNumber("TRACK-DUPLICADO")).thenReturn(true);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            orderService.update(1L, orderDetails);
        });

        assertEquals("Tracking is already assigned to another order", exception.getMessage());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void update_shouldReturnEmpty_WhenOrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.update(1L, new Order());

        assertTrue(result.isEmpty());
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void delete_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.delete(1L);

        assertTrue(result.isPresent(), "Debe devolver el pedido borrado");
        assertEquals(1L, result.get().getId());
        verify(orderRepository).delete(order);
    }

    @Test
    void delete_shouldReturnEmpty_WhenOrderDoesNotExist() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.delete(1L);

        assertTrue(result.isEmpty());
        verify(orderRepository, never()).delete(any(Order.class));
    }

}

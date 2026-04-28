package com.diego.mi_primer_api.controllers;

import com.diego.mi_primer_api.entities.Product;
import com.diego.mi_primer_api.services.ProductService;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        product1 = new Product("LPT-1234-10", "productTest1", "descriptionTest1", BigDecimal.valueOf(1000.99));
        product2 = new Product("TPL-4321-20", "productTest2", "descriptionTest2", BigDecimal.valueOf(2000.99));
        product1.setId(1L);
        product2.setId(2L);
    }

    @Test
    void findById_shouldReturnProduct_WhenIdExists(){
        when(productService.findById(1L)).thenReturn(Optional.of(product1));

        ResponseEntity<?> response = productController.viewProductById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Product body = (Product) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getProductName()).isEqualTo("productTest1");
        assertThat(body.getProductSKU()).isEqualTo("LPT-1234-10");

        verify(productService).findById(1L);
    }

    @Test
    void findById_shouldReturn404_whenIdNotFound() {
        when(productService.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = productController.viewProductById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(productService).findById(99L);
    }

    @Test
    void findAll_shouldReturnClients_whenIdExists() {
        when(productService.findAll()).thenReturn(Arrays.asList(product1, product2));

        ResponseEntity<?> response = productController.listAllProducts();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Product> body = (List<Product>) response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.size()).isEqualTo(2);
        assertThat(body).contains(product1, product2);

        verify(productService).findAll();
    }

    @Test
    void createProduct_shouldReturnCreatedProduct_WhenProductIsValid(){
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        product1.setId(null);

        Product productGuardadoEnDB = new Product("LPT-1234-10", "productTest1", "descriptionTest1", BigDecimal.valueOf(1000.99));

        productGuardadoEnDB.setId(1L);

        when(productService.save(any(Product.class))).thenReturn(productGuardadoEnDB);

        ResponseEntity<?> response = productController.createProduct(product1, result);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Product body = (Product) response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getProductName()).isEqualTo("productTest1");
        assertThat(body.getProductSKU()).isEqualTo("LPT-1234-10");

        verify(productService).save(any(Product.class));
    }

    @Test
    void createProduct_shouldReturnBadRequest_WhenBindingResultHasErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = productController.createProduct(product1, result);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(productService);
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct_WhenProductExistsAndIsValid(){
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        Long id = 1L;
        Product productUpdated = new Product("LPT-1234-10", "productTest1Actualizado", "descriptionTest1", BigDecimal.valueOf(1000.99));
        productUpdated.setId(id);

        when(productService.update(eq(id), any(Product.class))).thenReturn(Optional.of(productUpdated));

        ResponseEntity<?> response = productController.updateProduct(product1, result, id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Product body = (Product) response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getProductName()).isEqualTo("productTest1Actualizado");

        verify(productService).update(eq(id), any(Product.class));
    }

    @Test
    void updateProduct_shouldReturnBadRequest_WhenBindingResultHasErrors() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = productController.updateProduct(product1, result, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(productService);
    }

    @Test
    void updateProduct_shouldReturnNotFound_WhenIdNotFound() {
        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        when(productService.update(eq(1L), any(Product.class))).thenReturn(Optional.empty());

        ResponseEntity<?> response = productController.updateProduct(product1, result, 1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(productService).update(eq(1L), any(Product.class));
    }

    @Test
    void deleteProduct_shouldReturnDeletedProduct_WhenProductExistsAndIsValid(){
        Long id = 1L;

        when(productService.delete(id)).thenReturn(Optional.of(product1));

        ResponseEntity<?> response = productController.deleteProduct(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        verify(productService).delete(id);
    }

    @Test
    void deleteProduct_shouldReturnNotFound_WhenIdNotFound() {
        when(productService.delete(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = productController.deleteProduct(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        verify(productService).delete(1L);
    }

}















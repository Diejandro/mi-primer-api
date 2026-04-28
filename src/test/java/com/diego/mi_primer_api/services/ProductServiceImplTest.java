package com.diego.mi_primer_api.services;

import com.diego.mi_primer_api.entities.Product;
import com.diego.mi_primer_api.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("LPT-1234-10", "productTest", "descriptionTest", BigDecimal.valueOf(1000.99));
        product.setId(1L);
    }

    @Test
    void findById_shouldReturnProduct_WhenIdExists(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.findById(1L);

        assertTrue(result.isPresent(), "El resultado debe contener el producto");
        assertEquals("productTest", result.get().getProductName(), "El nombre debe coincidir con el del setup");
        assertEquals("LPT-1234-10", result.get().getProductSKU(), "El sku debe coincidir con el del setup");

        verify(productRepository).findById(1L);
    }

    @Test
    void findById_shouldNotReturnProduct_WhenIdDoesNotExist(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.findById(1L);

        assertFalse(result.isPresent(), "El resultado debe estar vacío");

        verify(productRepository).findById(1L);
    }

    @Test
    void save_shouldSaveProduct_WhenIdNotExists(){
        product.setId(null);

        Product productGuardadoDb = new Product("TPL-4321-01", "productTestSave", "descriptionTestSave", BigDecimal.valueOf(2000.99));
        productGuardadoDb.setId(1L);

        when(productRepository.save(any(Product.class))).thenReturn(productGuardadoDb);

        Product savedProduct = productService.save(product);

        assertNotNull(savedProduct);
        assertEquals(1L, savedProduct.getId());
        assertEquals("productTestSave", savedProduct.getProductName());

        verify(productRepository).save(any(Product.class));
    }

    @Test
    void save_shouldThrowException_WhenSkuAlreadyExists() {
        String skuExistente = product.getProductSKU();
        product.setProductSKU(skuExistente);

        when(productRepository.existsBySku(skuExistente)).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productService.save(product);
        });

        assertEquals("Ya existe un producto con el código: " + skuExistente, exception.getMessage());

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct_WhenIdExists(){
        Long id = 1L;
        Product productNuevo = new Product("TPL-4321-01", "productTestSave", "descriptionTestSave", BigDecimal.valueOf(2000.99));

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(productNuevo);

        Optional<Product> result = productService.update(id,  productNuevo);

        assertTrue(result.isPresent());
        assertEquals("productTestSave", result.get().getProductName());

        verify(productRepository).findById(id);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void updateProduct_shouldReturnEmpty_WhenIdDoesNotExists(){
        Long id = 1L;
        Product productNuevo = new Product("TPL-4321-01", "productTestSave", "descriptionTestSave", BigDecimal.valueOf(2000.99));

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Product> result = productService.update(id,  productNuevo);

        assertFalse(result.isPresent());

        verify(productRepository).findById(id);
        verify(productRepository, never()).save(any(Product.class));

    }

    @Test
    void deleteById_shouldDeleteProduct_WhenIdExists(){
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        Optional<Product> result = productService.delete(id);

        assertTrue(result.isPresent());
        assertEquals("productTest", result.get().getProductName());

        verify(productRepository).findById(id);
        verify(productRepository).delete(product);
    }

    @Test
    void deleteById_shouldNotDeleteProduct_WhenIdDoesNotExists(){
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Product> result = productService.delete(id);

        assertFalse(result.isPresent());

        verify(productRepository).findById(id);
        verify(productRepository, never()).delete(any());
    }


}


















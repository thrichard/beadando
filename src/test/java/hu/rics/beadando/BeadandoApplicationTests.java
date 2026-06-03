package hu.rics.beadando;

import hu.rics.beadando.model.Product;
import hu.rics.beadando.repository.ProductRepository;
import hu.rics.beadando.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeadandoApplicationTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(299999.99);
        product.setQuantity(5);
    }

    @Test
    void findAll_returnsAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> result = productService.findAll();
        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getName());
    }

    @Test
    void findById_returnsProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.findById(1L);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void save_returnsProduct() {
        when(productRepository.save(product)).thenReturn(product);
        Product result = productService.save(product);
        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void delete_callsRepository() {
        productService.delete(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void update_returnsUpdatedProduct() {
        Product updated = new Product();
        updated.setName("Gaming Laptop");
        updated.setPrice(499999.99);
        updated.setQuantity(3);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.update(1L, updated);
        assertNotNull(result);
    }

    @Test
    void findById_throwsException_whenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> productService.findById(99L));
    }

    @Test
    void findAll_returnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());
        List<Product> result = productService.findAll();
        assertEquals(0, result.size());
    }

    @Test
    void save_persistsProduct() {
        Product newProduct = new Product();
        newProduct.setName("Phone");
        newProduct.setPrice(99999.99);
        newProduct.setQuantity(10);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        Product result = productService.save(newProduct);
        assertEquals("Phone", result.getName());
    }

    @Test
    void delete_nonExistent_callsRepository() {
        productService.delete(99L);
        verify(productRepository, times(1)).deleteById(99L);
    }
}
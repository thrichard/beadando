package hu.rics.beadando;

import hu.rics.beadando.controller.ProductController;
import hu.rics.beadando.model.Product;
import hu.rics.beadando.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getAll_returnsEmptyList() throws Exception {
        when(productService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getById_returnsProduct() throws Exception {
        Product p = new Product();
        p.setId(1L);
        p.setName("Laptop");
        p.setPrice(299999.99);
        p.setQuantity(5);
        when(productService.findById(1L)).thenReturn(p);
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void create_returnsProduct() throws Exception {
        Product p = new Product();
        p.setId(1L);
        p.setName("Laptop");
        p.setPrice(299999.99);
        p.setQuantity(5);
        when(productService.save(any(Product.class))).thenReturn(p);
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Laptop\",\"price\":299999.99,\"quantity\":5}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }
}
package com.example.crudgame_z.controller;

import com.example.crudgame_z.dto.product.ProductDTO;
import com.example.crudgame_z.dto.product.ProductUpdateDTO;
import com.example.crudgame_z.dto.product.ProductCreateDTO;
import com.example.crudgame_z.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Obtener todos
    @GetMapping
    public List<ProductDTO> getAll() {
        return productService.getAll();
    }

    // Obtener por ID
    @GetMapping("/{id}")
    public ProductDTO getById(@PathVariable UUID id) {
        return productService.getById(id);
    }

    // Crear producto
    @PostMapping
    public ProductDTO create(@Valid @RequestBody ProductCreateDTO dto) {
        return productService.create(dto);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable UUID id, @RequestBody ProductUpdateDTO dto) {
        return productService.update(id, dto);
    }

    // Eliminar producto
    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) {
        productService.delete(id);
        return "Producto eliminado";
    }

    // 🔍 Barra de búsqueda
    @GetMapping("/search")
    public List<ProductDTO> search(@RequestParam String query) {
        return productService.search(query);
    }
}

package com.example.crudgame_z.service;

import com.example.crudgame_z.dto.product.ProductCreateDTO;
import com.example.crudgame_z.dto.product.ProductDTO;
import com.example.crudgame_z.dto.product.ProductUpdateDTO;
import com.example.crudgame_z.model.Product;
import com.example.crudgame_z.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Convertir a DTO
    private ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getNombre(),
                product.getPrecio(),
                product.getGenero(),
                product.getDescripcion(),
                product.getImageUrl(),
                product.getStock()
        );
    }

    // Obtener todos
    public List<ProductDTO> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Obtener por ID
    public ProductDTO getById(UUID id) {
        return productRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Crear producto
    public ProductDTO create(ProductCreateDTO dto) {
        Product product = new Product();
        product.setNombre(dto.getNombre());
        product.setPrecio(dto.getPrecio());
        product.setGenero(dto.getGenero());
        product.setDescripcion(dto.getDescripcion());
        product.setImageUrl(dto.getImageUrl());
        product.setStock(dto.getStock());

        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    // Actualizar producto
    public ProductDTO update(UUID id, ProductUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (dto.getNombre() != null) product.setNombre(dto.getNombre());
        if (dto.getPrecio() != null) product.setPrecio(dto.getPrecio());
        if (dto.getGenero() != null) product.setGenero(dto.getGenero());
        if (dto.getDescripcion() != null) product.setDescripcion(dto.getDescripcion());
        if (dto.getImageUrl() != null) product.setImageUrl(dto.getImageUrl());
        if (dto.getStock() != null) product.setStock(dto.getStock());

        Product saved = productRepository.save(product);
        return toDTO(saved);
    }

    // Eliminar
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productRepository.deleteById(id);
    }

    // 🔍 BUSCAR por nombre, genero o descripción
    public List<ProductDTO> search(String query) {
        return productRepository.search(query)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}

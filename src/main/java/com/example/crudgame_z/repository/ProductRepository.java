package com.example.crudgame_z.repository;

import com.example.crudgame_z.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    // 🔍 Barra de búsqueda completa: nombre, género y descripción
    @Query("SELECT p FROM Product p " +
           "WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.genero) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.descripcion) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> search(@Param("query") String query);
}

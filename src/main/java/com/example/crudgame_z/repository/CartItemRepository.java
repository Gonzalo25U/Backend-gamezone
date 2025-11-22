package com.example.crudgame_z.repository;

import com.example.crudgame_z.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    // Obtener carrito de un usuario
    List<CartItem> findByUserId(UUID userId);

    // Para verificar si un producto ya está en el carrito
    Optional<CartItem> findByUserIdAndProductId(UUID userId, UUID productId);

    // Borrar carrito completo del usuario
    void deleteByUserId(UUID userId);
}
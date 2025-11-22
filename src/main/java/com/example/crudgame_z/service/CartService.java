package com.example.crudgame_z.service;

import com.example.crudgame_z.dto.cart.CartItemRequest;
import com.example.crudgame_z.dto.cart.CartItemResponse;
import com.example.crudgame_z.exception.ForbiddenException;
import com.example.crudgame_z.model.CartItem;
import com.example.crudgame_z.model.Product;
import com.example.crudgame_z.model.User;
import com.example.crudgame_z.repository.CartItemRepository;
import com.example.crudgame_z.repository.ProductRepository;
import com.example.crudgame_z.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            ProductRepository productRepository
    ) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    // Obtener usuario autenticado
    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
    }

    // Convertir CartItem → CartItemResponse
    private CartItemResponse toResponse(CartItem item, Product product) {
        return new CartItemResponse(
                item.getId(),
                product.getId(),
                product.getNombre(),
                item.getQuantity(),
                product.getPrecio().intValue()
        );
    }

    // Verificar dueño del item
    private void validateOwner(User user, CartItem item) {
        if (!item.getUserId().equals(user.getId())) {
            throw new ForbiddenException("No tienes permiso para modificar este item");
        }
    }

    // Obtener carrito del usuario
    public List<CartItemResponse> getCartForUser(String email) {

        User user = getUser(email);

        return cartItemRepository.findByUserId(user.getId())
                .stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                    return toResponse(item, product);
                })
                .collect(Collectors.toList());
    }

    // Agregar producto al carrito
    public CartItemResponse addItem(String email, CartItemRequest request) {

        User user = getUser(email);

        UUID productId = request.getProductId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no existe"));

        // Si ya existe → sumar cantidades
        CartItem existing = cartItemRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
            cartItemRepository.save(existing);
            return toResponse(existing, product);
        }

        // Crear nuevo
        CartItem newItem = new CartItem(user.getId(), productId, request.getQuantity());
        cartItemRepository.save(newItem);

        return toResponse(newItem, product);
    }

    // Actualizar cantidad
    public CartItemResponse updateItem(String email, UUID itemId, CartItemRequest request) {

        User user = getUser(email);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        validateOwner(user, item);

        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);

        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        return toResponse(item, product);
    }

    // Eliminar item
    public void deleteItem(String email, UUID itemId) {

        User user = getUser(email);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));

        validateOwner(user, item);

        cartItemRepository.delete(item);
    }
}

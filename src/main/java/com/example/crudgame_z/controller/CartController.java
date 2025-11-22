package com.example.crudgame_z.controller;

import com.example.crudgame_z.dto.cart.CartItemRequest;
import com.example.crudgame_z.dto.cart.CartItemResponse;
import com.example.crudgame_z.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    // Obtener carrito del usuario
    @GetMapping
    public List<CartItemResponse> getCart(Principal principal) {
        return cartService.getCartForUser(principal.getName());
    }


    // Agregar item al carrito
    @PostMapping
    public CartItemResponse addToCart(
            @Valid @RequestBody CartItemRequest request,
            Principal principal
    ) {
        return cartService.addItem(principal.getName(), request);
    }


    // Actualizar cantidad
    @PutMapping("/{itemId}")
    public CartItemResponse updateQuantity(
            @PathVariable UUID itemId,
            @Valid @RequestBody CartItemRequest request,
            Principal principal
    ) {
        return cartService.updateItem(principal.getName(), itemId, request);
    }

    // Eliminar item
    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable UUID itemId, Principal principal) {
        cartService.deleteItem(principal.getName(), itemId);
    }
}
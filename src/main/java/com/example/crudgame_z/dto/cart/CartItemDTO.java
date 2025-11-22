package com.example.crudgame_z.dto.cart;

import java.util.UUID;

public class CartItemDTO {

    private UUID id;
    private UUID productId;
    private String nombre;
    private Double precio;
    private String imagen;
    private Integer quantity;

    public CartItemDTO(UUID id, UUID productId, String nombre, Double precio, String imagen, Integer quantity) {
        this.id = id;
        this.productId = productId;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.quantity = quantity;
    }

    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public String getNombre() { return nombre; }
    public Double getPrecio() { return precio; }
    public String getImagen() { return imagen; }
    public Integer getQuantity() { return quantity; }
}
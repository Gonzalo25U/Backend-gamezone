package com.example.crudgame_z.dto.cart;

import java.util.UUID;

public class CartItemResponse {

    private UUID id;
    private UUID productId;
    private String productName;
    private Integer quantity;
    private Integer price;

    public CartItemResponse(UUID id, UUID productId, String productName, Integer quantity, Integer price) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public Integer getPrice() { return price; }
}
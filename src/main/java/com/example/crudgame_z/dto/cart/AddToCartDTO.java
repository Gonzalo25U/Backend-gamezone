package com.example.crudgame_z.dto.cart;

import java.util.UUID;

public class AddToCartDTO {

    private UUID productId;
    private Integer quantity;

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
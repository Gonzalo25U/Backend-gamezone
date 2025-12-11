package com.example.crudgame_z.dto.users;

import com.example.crudgame_z.model.Role;
import java.time.Instant;
import java.util.UUID;

public class UserResponseDTO {

    private UUID id;
    private String email;
    private String name;
    private Role role;
    private Instant createdAt;

    public UserResponseDTO(UUID id, String email, String name, Role role, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
        this.createdAt = createdAt;
    }

    // getters
    public UUID getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public Role getRole() {
        return role;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    
}

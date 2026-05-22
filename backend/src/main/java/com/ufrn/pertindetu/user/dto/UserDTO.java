package com.ufrn.pertindetu.user.dto;

import com.ufrn.pertindetu.base.dto.EntityDTO;
import com.ufrn.pertindetu.user.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDTO implements EntityDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;

    @Size(max = 20)
    private String phone;

    private boolean active;
    private String createdAt;

    @Override
    public EntityDTO toResponse() {
        UserDTO response = new UserDTO();
        response.id = this.id;
        response.name = this.name;
        response.email = this.email;
        response.role = this.role;
        response.phone = this.phone;
        response.active = this.active;
        response.createdAt = this.createdAt;
        return response;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
package com.ufrn.pertindetu.user.dto;

import com.ufrn.pertindetu.base.dto.EntityDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordResetRequestDTO implements EntityDTO {

    @NotBlank
    @Email
    private String email;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public EntityDTO toResponse() { return this; }
}


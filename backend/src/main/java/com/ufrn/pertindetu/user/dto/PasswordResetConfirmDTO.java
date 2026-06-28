package com.ufrn.pertindetu.user.dto;

import com.ufrn.pertindetu.base.dto.EntityDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordResetConfirmDTO implements EntityDTO {

    @NotBlank
    private String token;

    @NotBlank
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String newPassword;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    @Override
    public EntityDTO toResponse() { return this; }
}


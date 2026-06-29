package com.ufrn.pertindetu.provider.dto;

import com.ufrn.pertindetu.base.dto.EntityDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProviderProfileDTO implements EntityDTO {

    private Long id;

    @NotBlank(message = "A categoria principal é obrigatória.")
    private String category;

    @Size(max = 1000, message = "A biografia deve ter no máximo 1000 caracteres.")
    private String bio;

    @NotBlank(message = "O WhatsApp de contato é obrigatório.")
    private String whatsappPhone;

    @NotBlank(message = "O bairro é obrigatório.")
    private String neighborhood;

    @NotBlank(message = "A cidade é obrigatória.")
    private String city;

    @NotBlank(message = "O UF é obrigatório.")
    @Size(min = 2, max = 2)
    private String state;

    private boolean active;
    private String createdAt;

    @Override
    public EntityDTO toResponse() {
        ProviderProfileDTO response = new ProviderProfileDTO();
        response.id = this.id;
        response.category = this.category;
        response.bio = this.bio;
        response.whatsappPhone = this.whatsappPhone;
        response.neighborhood = this.neighborhood;
        response.city = this.city;
        response.state = this.state;
        response.active = this.active;
        response.createdAt = this.createdAt;
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWhatsappPhone() {
        return whatsappPhone;
    }

    public void setWhatsappPhone(String whatsappPhone) {
        this.whatsappPhone = whatsappPhone;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
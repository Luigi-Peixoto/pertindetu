package com.ufrn.pertindetu.provider.dto;

import com.ufrn.pertindetu.base.dto.EntityDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
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
}
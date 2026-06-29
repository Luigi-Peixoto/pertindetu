package com.ufrn.pertindetu.provider.model;

import com.ufrn.pertindetu.base.model.BaseEntity;
import com.ufrn.pertindetu.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Professional "vitrine" of a provider.
 * Timestamps, soft-delete and identity helpers come from {@link BaseEntity}.
 */
@Entity
@Table(name = "tb_provider_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String category; // Ex: "Bolos Regionais", "Maquiagem"

    @Column(columnDefinition = "TEXT")
    private String bio; // A história do talento

    @Column(name = "whatsapp_phone", nullable = false)
    private String whatsappPhone;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 2)
    private String state; // Ex: "RN", "PB", "PE"
}
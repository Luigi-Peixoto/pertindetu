package com.ufrn.pertindetu.provider.model;

import com.ufrn.pertindetu.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_provider_profiles")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProviderProfile {

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
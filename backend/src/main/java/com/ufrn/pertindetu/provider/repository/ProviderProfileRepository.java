package com.ufrn.pertindetu.provider.repository;

import com.ufrn.pertindetu.provider.model.ProviderProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderProfileRepository extends JpaRepository<ProviderProfile, Long> {

    // Busca a vitrine baseada no ID do usuário logado
    Optional<ProviderProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
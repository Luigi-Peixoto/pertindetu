package com.ufrn.pertindetu.provider.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.provider.model.ProviderProfile;

@Repository
public interface ProviderProfileRepository
        extends GenericRepository<ProviderProfile> {

    Optional<ProviderProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
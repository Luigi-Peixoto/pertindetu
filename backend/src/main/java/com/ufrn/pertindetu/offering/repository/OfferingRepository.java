package com.ufrn.pertindetu.offering.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.offering.model.Offering;

@Repository
public interface OfferingRepository extends GenericRepository<Offering> {

    List<Offering> findByCategoryIgnoreCase(String category);

    List<Offering> findByNeighborhoodIgnoreCase(String neighborhood);

    List<Offering> findByProviderId(Long providerId);
}
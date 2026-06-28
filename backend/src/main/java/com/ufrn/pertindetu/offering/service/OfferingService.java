package com.ufrn.pertindetu.offering.service;

import org.springframework.stereotype.Service;

import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.base.service.GenericService;
import com.ufrn.pertindetu.offering.dto.OfferingDTO;
import com.ufrn.pertindetu.offering.mapper.OfferingMapper;
import com.ufrn.pertindetu.offering.model.Offering;
import com.ufrn.pertindetu.offering.repository.OfferingRepository;

@Service
public class OfferingService implements GenericService<Offering, OfferingDTO> {

    private final OfferingRepository repository;
    private final OfferingMapper mapper;

    public OfferingService(OfferingRepository repository, OfferingMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public GenericRepository<Offering> getRepository() {
        return repository;
    }

    @Override
    public DtoMapper<Offering, OfferingDTO> getDtoMapper() {
        return mapper;
    }
}
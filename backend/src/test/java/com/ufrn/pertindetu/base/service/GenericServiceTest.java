package com.ufrn.pertindetu.base.service;

import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.base.model.BaseEntity;
import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.base.utils.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class GenericServiceTest {

    static class TestEntity extends BaseEntity {
        private Long id;

        @Override
        public Long getId() { return id; }

        @Override
        public void setId(Long id) { this.id = id; }
    }

    static class TestDTO {
        private Long id;
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
    }

    static class TestService implements GenericService<TestEntity, TestDTO> {
        private final GenericRepository<TestEntity> repo;
        private final DtoMapper<TestEntity, TestDTO> mapper;

        TestService(GenericRepository<TestEntity> repo, DtoMapper<TestEntity, TestDTO> mapper) {
            this.repo = repo;
            this.mapper = mapper;
        }

        @Override
        public GenericRepository<TestEntity> getRepository() { return repo; }

        @Override
        public DtoMapper<TestEntity, TestDTO> getDtoMapper() { return mapper; }
    }

    @Mock
    private GenericRepository<TestEntity> repository;

    @Mock
    private DtoMapper<TestEntity, TestDTO> mapper;

    private TestService service;
    private TestEntity entity;
    private TestDTO dto;

    @BeforeEach
    void setUp() {
        service = new TestService(repository, mapper);

        entity = new TestEntity();
        entity.setId(1L);
        entity.setActive(true);

        dto = new TestDTO();
        dto.setId(1L);
    }


    @Test
    void findAll_shouldReturnPageOfDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(entityPage);
        when(mapper.toDto(List.of(entity))).thenReturn(List.of(dto));

        Page<TestDTO> result = service.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository).findAll(pageable);
    }

    @Test
    void findAll_shouldReturnEmptyPage_whenNoEntities() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TestEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(repository.findAll(pageable)).thenReturn(emptyPage);
        when(mapper.toDto(List.of())).thenReturn(List.of());

        Page<TestDTO> result = service.findAll(pageable);

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    void findById_shouldReturnDto_whenEntityExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        TestDTO result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_shouldThrowResourceNotFoundException_whenEntityNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Id not found: 99");
    }

    @Test
    void deleteById_shouldSetActiveFalse_whenEntityExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(any(TestEntity.class))).thenReturn(entity);

        service.deleteById(1L);

        assertThat(entity.isActive()).isFalse();
        verify(repository).save(entity);
    }

    @Test
    void deleteById_shouldThrowResourceNotFoundException_whenEntityNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Id not found: 99");
    }

    @Test
    void update_shouldThrowResourceNotFoundException_whenEntityNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(99L, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Id not found: 99");
    }

    @Test
    void update_shouldPreserveCreatedAt_fromExistingEntity() {
        ZonedDateTime originalCreatedAt = ZonedDateTime.now().minusDays(1);
        entity.setCreatedAt(originalCreatedAt);

        TestEntity updatedEntity = new TestEntity();
        updatedEntity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toEntity(dto)).thenReturn(updatedEntity);
        when(repository.save(any(TestEntity.class))).thenReturn(updatedEntity);
        when(mapper.toDto(updatedEntity)).thenReturn(dto);

        service.update(1L, dto);

        assertThat(updatedEntity.getCreatedAt()).isEqualTo(originalCreatedAt);
    }
}
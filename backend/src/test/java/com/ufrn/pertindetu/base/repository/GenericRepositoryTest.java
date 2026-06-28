package com.ufrn.pertindetu.base.repository;

import com.ufrn.pertindetu.base.model.BaseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class GenericRepositoryTest {

    static class TestEntity extends BaseEntity {
        private Long id;
        @Override public Long getId() { return id; }
        @Override public void setId(Long id) { this.id = id; }
    }

    static class TestRepositoryImpl implements GenericRepository<TestEntity> {
        List<TestEntity> store = new ArrayList<>();

        @Override
        public Optional<TestEntity> findById(Long id) {
            return store.stream().filter(e -> e.getId().equals(id)).findFirst();
        }

        @Override
        public <S extends TestEntity> S save(S entity) {
            store.removeIf(e -> e.getId().equals(entity.getId()));
            store.add(entity);
            return entity;
        }

        @Override public List<TestEntity> findAll() { return store; }
        @Override public void deleteById(Long id) { GenericRepository.super.deleteById(id); }
        @Override public void delete(TestEntity obj) { GenericRepository.super.delete(obj); }
        @Override public void deleteAll(Iterable<? extends TestEntity> arg0) { GenericRepository.super.deleteAll(arg0); }
        @Override public <S extends TestEntity> List<S> saveAll(Iterable<S> entities) { throw new UnsupportedOperationException(); }
        @Override public boolean existsById(Long id) { throw new UnsupportedOperationException(); }
        @Override public List<TestEntity> findAllById(Iterable<Long> ids) { throw new UnsupportedOperationException(); }
        @Override public long count() { throw new UnsupportedOperationException(); }
        @Override public void deleteAllById(Iterable<? extends Long> ids) { throw new UnsupportedOperationException(); }
        @Override public void deleteAll() { throw new UnsupportedOperationException(); }
        @Override public org.springframework.data.domain.Page<TestEntity> findAll(org.springframework.data.domain.Pageable pageable) { throw new UnsupportedOperationException(); }
        @Override public List<TestEntity> findAll(org.springframework.data.domain.Sort sort) { throw new UnsupportedOperationException(); }
        @Override public void flush() {}
        @Override public <S extends TestEntity> S saveAndFlush(S entity) { return save(entity); }
        @Override public <S extends TestEntity> List<S> saveAllAndFlush(Iterable<S> entities) { throw new UnsupportedOperationException(); }
        @Override public void deleteAllInBatch(Iterable<TestEntity> entities) { throw new UnsupportedOperationException(); }
        @Override public void deleteAllByIdInBatch(Iterable<Long> ids) { throw new UnsupportedOperationException(); }
        @Override public void deleteAllInBatch() { throw new UnsupportedOperationException(); }
        @Override public TestEntity getOne(Long id) { throw new UnsupportedOperationException(); }
        @Override public TestEntity getById(Long id) { throw new UnsupportedOperationException(); }
        @Override public TestEntity getReferenceById(Long id) { throw new UnsupportedOperationException(); }
        @Override public <S extends TestEntity> Optional<S> findOne(org.springframework.data.domain.Example<S> example) { throw new UnsupportedOperationException(); }
        @Override public <S extends TestEntity> List<S> findAll(org.springframework.data.domain.Example<S> example) { throw new UnsupportedOperationException(); }
        @Override public <S extends TestEntity> List<S> findAll(org.springframework.data.domain.Example<S> example, org.springframework.data.domain.Sort sort) { throw new UnsupportedOperationException(); }
        @Override public <S extends TestEntity> org.springframework.data.domain.Page<S> findAll(org.springframework.data.domain.Example<S> example, org.springframework.data.domain.Pageable pageable) { throw new UnsupportedOperationException(); }
        @Override public <S extends TestEntity> long count(org.springframework.data.domain.Example<S> example) { throw new UnsupportedOperationException(); }
        @Override public <S extends TestEntity> boolean exists(org.springframework.data.domain.Example<S> example) { throw new UnsupportedOperationException(); }
        @Override public <S extends TestEntity, R> R findBy(org.springframework.data.domain.Example<S> example, java.util.function.Function<org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery<S>, R> queryFunction) { throw new UnsupportedOperationException(); }
    }

    private TestRepositoryImpl repository;
    private TestEntity entity;

    @BeforeEach
    void setUp() {
        repository = new TestRepositoryImpl();
        entity = new TestEntity();
        entity.setId(1L);
        entity.setActive(true);
        repository.store.add(entity);
    }

    @Test
    void delete_shouldSetActiveFalse() {
        repository.delete(entity);
        assertThat(entity.isActive()).isFalse();
    }

    @Test
    void deleteById_shouldSetActiveFalse_whenEntityExists() {
        repository.deleteById(1L);
        assertThat(entity.isActive()).isFalse();
    }

    @Test
    void deleteAll_shouldSetActiveFalseForAllEntities() {
        TestEntity entity2 = new TestEntity();
        entity2.setId(2L);
        entity2.setActive(true);
        repository.store.add(entity2);

        repository.deleteAll(List.of(entity, entity2));

        assertThat(entity.isActive()).isFalse();
        assertThat(entity2.isActive()).isFalse();
    }
}
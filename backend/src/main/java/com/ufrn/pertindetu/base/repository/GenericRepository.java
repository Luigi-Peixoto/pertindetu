package com.ufrn.pertindetu.base.repository;

import com.ufrn.pertindetu.base.model.BaseEntity;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * A generic repository interface defining common CRUD operations for entities
 * in the application.
 *
 * @param <T> The type of the entity extending BaseEntity.
 */
@NoRepositoryBean
public interface GenericRepository<T extends BaseEntity> extends JpaRepository<T, Long> {

    /**
     * Overrides the default deleteById method to mark the entity as deleted instead
     * of physically removing it.
     *
     * @param id The ID of the entity to be marked as deleted.
     */
    @Override
    @Transactional
    default void deleteById(@NonNull Long id) {
        Optional<T> entity = findById(id);
        if (entity.isPresent()) {
            entity.get().setActive(false);
            save(entity.get());
        }
    }

    /**
     * Overrides the default delete method to mark the entity as deleted instead of
     * physically removing it.
     *
     * @param obj The entity to be marked as deleted.
     */
    @Override
    @Transactional
    default void delete(T obj) {
        obj.setActive(false);
        save(obj);
    }

    /**
     * Overrides the default deleteAll method to mark the entities as deleted
     * instead of physically removing them.
     *
     * @param arg0 Iterable of entities to be marked as deleted.
     */
    @Override
    @Transactional
    default void deleteAll(Iterable<? extends T> arg0) {
        arg0.forEach(entity -> deleteById(entity.getId()));
    }

}


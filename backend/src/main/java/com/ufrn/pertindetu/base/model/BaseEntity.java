package com.ufrn.pertindetu.base.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Mapped superclass for entities. Provides creation/update timestamps and soft-delete.
 * <p>
 * Each concrete entity must declare its own {@code id} with a dedicated sequence (one per table):
 * <pre>
 * &#64;Id
 * &#64;GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "minha_entidade_seq")
 * &#64;SequenceGenerator(name = "minha_entidade_seq", sequenceName = "minha_tabela_id_seq", allocationSize = 1)
 * private Long id;
 * </pre>
 * Create the sequence in Flyway, e.g. {@code CREATE SEQUENCE minha_tabela_id_seq;}
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Column(name = "created_at", nullable = false, updatable = false)
    protected ZonedDateTime createdAt;

    @Column(name = "updated_at")
    protected ZonedDateTime updatedAt;

    @Column(name = "active")
    protected boolean active = true;

    public abstract Long getId();

    public abstract void setId(Long id);

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Sets the creation timestamp before persisting the entity.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
    }

    /**
     * Sets the update timestamp before updating the entity.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    /**
     * Equality based on id only (JPA entity identity).
     * Transient entities (id == null) are compared by reference.
     * Soft-delete (active) does not change identity.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        BaseEntity that = (BaseEntity) o;
        Long myId = getId();
        Long thatId = that.getId();
        if (myId == null || thatId == null)
            return false;
        return myId.equals(thatId);
    }

    @Override
    public int hashCode() {
        Long id = getId();
        return id == null ? super.hashCode() : Objects.hash(id);
    }
}

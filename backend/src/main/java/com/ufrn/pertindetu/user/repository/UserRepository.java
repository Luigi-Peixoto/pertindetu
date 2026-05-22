package com.ufrn.pertindetu.user.repository;

import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GenericRepository<User> {

    Optional<User> findByEmailAndActiveTrue(String email);

    Optional<User> findByResetToken(String resetToken);

    Optional<User> findByEmail(String email);

    boolean existsByEmailAndActiveTrue(String email);
}
package com.ufrn.pertindetu.user.service;

import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.base.repository.GenericRepository;
import com.ufrn.pertindetu.base.service.GenericService;
import com.ufrn.pertindetu.base.utils.exception.BusinessException;
import com.ufrn.pertindetu.user.dto.UserDTO;
import com.ufrn.pertindetu.user.mapper.UserMapper;
import com.ufrn.pertindetu.user.model.User;
import com.ufrn.pertindetu.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements GenericService<User, UserDTO> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public GenericRepository<User> getRepository() {
        return userRepository;
    }

    @Override
    public DtoMapper<User, UserDTO> getDtoMapper() {
        return userMapper;
    }

    @Override
    public UserDTO create(UserDTO dto) {
        if (userRepository.existsByEmailAndActiveTrue(dto.getEmail())) {
            throw new BusinessException("Email already registered.", HttpStatus.CONFLICT);
        }

        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BusinessException("Password is required.", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.toEntity(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found.", HttpStatus.NOT_FOUND));

        if (!existing.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmailAndActiveTrue(dto.getEmail())) {
            throw new BusinessException("Email already in use.", HttpStatus.CONFLICT);
        }

        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setRole(dto.getRole());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            existing.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }

        return userMapper.toDto(userRepository.save(existing));
    }
}
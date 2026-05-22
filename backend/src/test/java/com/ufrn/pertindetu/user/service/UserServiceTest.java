package com.ufrn.pertindetu.user.service;

import com.ufrn.pertindetu.base.utils.exception.BusinessException;
import com.ufrn.pertindetu.user.dto.UserDTO;
import com.ufrn.pertindetu.user.mapper.UserMapper;
import com.ufrn.pertindetu.user.model.User;
import com.ufrn.pertindetu.user.model.enums.UserRole;
import com.ufrn.pertindetu.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setName("User User");
        userDTO.setEmail("user@email.com");
        userDTO.setPassword("minhasenha123");
        userDTO.setRole(UserRole.CLIENT);
        userDTO.setPhone("84999999999");

        user = new User();
        user.setId(1L);
        user.setName("User User");
        user.setEmail("user@email.com");
        user.setRole(UserRole.CLIENT);
        user.setPhone("84999999999");
        user.setActive(true);
    }

    // -------------------------
    // CREATE
    // -------------------------

    @Test
    void create_shouldCreateUser_whenEmailNotRegistered() {
        when(userRepository.existsByEmailAndActiveTrue(userDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.create(userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("user@email.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void create_shouldThrowBusinessException_whenEmailAlreadyRegistered() {
        when(userRepository.existsByEmailAndActiveTrue(userDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.create(userDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email already registered.");
    }

    @Test
    void create_shouldThrowBusinessException_whenPasswordIsNull() {
        userDTO.setPassword(null);
        when(userRepository.existsByEmailAndActiveTrue(userDTO.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> userService.create(userDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Password is required.");
    }

    @Test
    void create_shouldThrowBusinessException_whenPasswordIsBlank() {
        userDTO.setPassword("   ");
        when(userRepository.existsByEmailAndActiveTrue(userDTO.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> userService.create(userDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Password is required.");
    }

    @Test
    void create_shouldEncodePassword_beforeSaving() {
        when(userRepository.existsByEmailAndActiveTrue(userDTO.getEmail())).thenReturn(false);
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

        userService.create(userDTO);

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPasswordHash() != null
                        && !savedUser.getPasswordHash().equals("minhasenha123")
        ));
    }

    // -------------------------
    // UPDATE
    // -------------------------

    @Test
    void update_shouldUpdateUser_whenUserExistsAndEmailUnchanged() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.update(1L, userDTO);

        assertThat(result).isNotNull();
        verify(userRepository).save(user);
    }

    @Test
    void update_shouldThrowBusinessException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(99L, userDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("User not found.");
    }

    @Test
    void update_shouldThrowBusinessException_whenNewEmailAlreadyInUse() {
        user.setEmail("outro@email.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndActiveTrue(userDTO.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.update(1L, userDTO))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email already in use.");
    }

    @Test
    void update_shouldUpdatePassword_whenNewPasswordProvided() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

        userDTO.setPassword("novasenha456");
        userService.update(1L, userDTO);

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPasswordHash() != null
                        && !savedUser.getPasswordHash().equals("novasenha456")
        ));
    }

    @Test
    void update_shouldNotUpdatePassword_whenPasswordIsNull() {
        String originalHash = "$2a$10$hashoriginal";
        user.setPasswordHash(originalHash);
        userDTO.setPassword(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toDto(any(User.class))).thenReturn(userDTO);

        userService.update(1L, userDTO);

        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPasswordHash().equals(originalHash)
        ));
    }
}
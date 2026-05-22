package com.ufrn.pertindetu.user.controller;
import tools.jackson.databind.ObjectMapper;
import com.ufrn.pertindetu.base.utils.exception.BusinessException;
import com.ufrn.pertindetu.security.utils.JwtService;
import com.ufrn.pertindetu.user.dto.UserDTO;
import com.ufrn.pertindetu.user.model.enums.UserRole;
import com.ufrn.pertindetu.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("User User");
        userDTO.setEmail("user@email.com");
        userDTO.setPassword("minhasenha123");
        userDTO.setRole(UserRole.CLIENT);
        userDTO.setPhone("84999999999");
        userDTO.setActive(true);
    }

    @Test
    @WithMockUser
    void create_shouldReturn201_whenUserCreatedSuccessfully() throws Exception {
        when(userService.create(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post("/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("user@email.com"))
                .andExpect(jsonPath("$.data.name").value("User User"));
    }

    @Test
    @WithMockUser
    void create_shouldReturn409_whenEmailAlreadyRegistered() throws Exception {
        when(userService.create(any(UserDTO.class)))
                .thenThrow(new BusinessException("Email already registered.", HttpStatus.CONFLICT));

        mockMvc.perform(post("/v1/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    void getById_shouldReturn200_whenUserExists() throws Exception {
        when(userService.findById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/v1/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("user@email.com"));
    }

    @Test
    @WithMockUser
    void getAll_shouldReturn200_withPageOfUsers() throws Exception {
        var page = new PageImpl<>(List.of(userDTO), PageRequest.of(0, 10), 1);
        when(userService.findAll(any())).thenReturn(page);

        mockMvc.perform(get("/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].email").value("user@email.com"));
    }

    @Test
    @WithMockUser
    void update_shouldReturn200_whenUserUpdatedSuccessfully() throws Exception {
        when(userService.update(eq(1L), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/v1/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("User User"));
    }

    @Test
    @WithMockUser
    void delete_shouldReturn200_whenUserDeletedSuccessfully() throws Exception {
        doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/v1/users/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
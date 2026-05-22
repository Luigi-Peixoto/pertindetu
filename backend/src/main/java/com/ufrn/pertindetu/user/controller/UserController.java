package com.ufrn.pertindetu.user.controller;

import com.ufrn.pertindetu.base.controller.GenericController;
import com.ufrn.pertindetu.user.dto.UserDTO;
import com.ufrn.pertindetu.user.model.User;
import com.ufrn.pertindetu.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserController extends GenericController<User, UserDTO, UserService> {

    public UserController(UserService service) {
        super(service);
    }
}
package com.ufrn.pertindetu.user.mapper;

import com.ufrn.pertindetu.base.mappers.BaseMapperConfig;
import com.ufrn.pertindetu.base.mappers.DtoMapper;
import com.ufrn.pertindetu.user.dto.UserDTO;
import com.ufrn.pertindetu.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapperConfig.class)
public interface UserMapper extends DtoMapper<User, UserDTO> {

    @Override
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "active", source = "active")
    UserDTO toDto(User user);

    @Override
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "active", ignore = true)
    User toEntity(UserDTO dto);
}
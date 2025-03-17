package com.example.shoppingCart.mapper;

import com.example.shoppingCart.entity.Role;
import com.example.shoppingCart.model.RoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);


    RoleModel rolesToRolesModel(Role role);

    List<RoleModel> rolesToRolesModel(List<Role> roles);

}

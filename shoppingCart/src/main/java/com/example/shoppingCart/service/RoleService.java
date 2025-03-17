package com.example.shoppingCart.service;

import com.example.shoppingCart.entity.Role;
import com.example.shoppingCart.mapper.RoleMapper;
import com.example.shoppingCart.model.RoleModel;
import com.example.shoppingCart.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    public List<RoleModel> getAllRoles(){
        List<Role> allRoles = roleRepository.findAll();
        return roleMapper.rolesToRolesModel(allRoles);

    }

}

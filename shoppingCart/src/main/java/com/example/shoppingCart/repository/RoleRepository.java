package com.example.shoppingCart.repository;

import com.example.shoppingCart.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAllByRoleIdIn(List<Long> roleId);


}

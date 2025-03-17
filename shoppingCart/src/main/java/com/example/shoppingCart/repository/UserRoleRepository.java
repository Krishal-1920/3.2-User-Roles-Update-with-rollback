package com.example.shoppingCart.repository;

import com.example.shoppingCart.entity.User;
import com.example.shoppingCart.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUser(User user);

}

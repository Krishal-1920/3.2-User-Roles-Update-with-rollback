package com.example.shoppingCart.controller;

import com.example.shoppingCart.model.RoleModel;
import com.example.shoppingCart.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("getAll")
    public ResponseEntity<List<RoleModel>> fetchingAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }

}

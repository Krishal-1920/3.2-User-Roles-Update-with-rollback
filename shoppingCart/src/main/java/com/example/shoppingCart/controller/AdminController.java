package com.example.shoppingCart.controller;

import com.example.shoppingCart.entity.User;
import com.example.shoppingCart.model.ProductModel;
import com.example.shoppingCart.service.AdminService;
import com.example.shoppingCart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    //******** For Product ********//

    // Get Product By Name
    @GetMapping("/getByName/{name}")
    public List<ProductModel> getByName(@PathVariable String name){
        return userService.findProductByName(name);
    }

    // Get Product By Category
    @GetMapping("/category")
    public ResponseEntity<List<ProductModel>> getByCategory(@RequestParam String category) {
        List<ProductModel> products = userService.findProductByCategory(category);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList()); // Return 404 if no products found
        }

        return ResponseEntity.ok(products);
    }

    //******** For User ********//

    // Get User By email, phoneNumber

    @GetMapping("/searchUser")
    public ResponseEntity<List<User>> fetchUserByDetail(@RequestParam(required = false) String email,
                                                    @RequestParam(required = false) String phoneNumber) {
        List<User> users = adminService.getUsers(email, phoneNumber);
        return ResponseEntity.ok(users);
    }
}


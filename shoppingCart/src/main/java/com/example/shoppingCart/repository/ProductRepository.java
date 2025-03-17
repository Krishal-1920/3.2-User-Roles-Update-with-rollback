package com.example.shoppingCart.repository;

import com.example.shoppingCart.entity.Product;
import com.example.shoppingCart.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Get Product By Name
    List<Product> findByProductName(String productName);

    // Get Product By Category
    List<Product> findByProductCategory(ProductCategory productCategory);

}

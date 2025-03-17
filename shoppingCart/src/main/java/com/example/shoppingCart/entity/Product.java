package com.example.shoppingCart.entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import com.example.shoppingCart.enums.ProductCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_category", nullable = false)
    private ProductCategory productCategory;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "product_price", nullable = false)
    private Long productPrice;


    @Column(name = "product_rating")
    @Min(value=0)
    @Max(value=5)
    private float productRating;
}

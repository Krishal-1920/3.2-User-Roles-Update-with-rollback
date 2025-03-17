package com.example.shoppingCart.model;

import com.example.shoppingCart.enums.ProductCategory;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String productName;
    private ProductCategory productCategory;
    private String productDescription;
    private Long productPrice;
    private float productRating;
}

package com.example.shoppingCart.service;


import com.example.shoppingCart.model.ProductModel;
import com.example.shoppingCart.entity.Product;
import com.example.shoppingCart.enums.ProductCategory;
import com.example.shoppingCart.mapper.ProductMapper;
import com.example.shoppingCart.repository.ProductRepository;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {


    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    // Adding The Product
    public ProductModel addProduct(ProductModel productModel){
        Product addProduct = productMapper.productModelToProduct(productModel);
        addProduct = productRepository.save(addProduct);
        ProductModel productReturnedToModel = productMapper.productToProductModel(addProduct);
        return productReturnedToModel;
    }

    // Getting All Products
    public List<ProductModel> fetchProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> productMapper.productToProductModel(product))
                .toList();
    }



    // Get Product By ID
    public ProductModel findProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        return productMapper.productToProductModel(product);
    }



    // Get Product By Name
    public List<ProductModel> findProductByName(String productName) {
        List<Product> products = productRepository.findByProductName(productName);
        List<ProductModel> productModels = new ArrayList<>();

        for (Product product : products) {
            productModels.add(productMapper.productToProductModel(product));
        }

        return productModels;
    }



    // Get Product By Category

    //    public List<Product> findProductByCategory(String categoryName) {
    //        ProductCategory category = ProductCategory.valueOf(categoryName.toUpperCase()); // Convert String to Enum
    //        return productRepository.findByProductCategory(ProductCategory.valueOf(String.valueOf(category)));
    //    }

    public List<ProductModel> findProductByCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return Collections.emptyList();
        }
        categoryName = categoryName.trim().toUpperCase();
        ProductCategory closestCategory = getBestMatchingCategory(categoryName);

        if (closestCategory != null) {
            List<Product> products = productRepository.findByProductCategory(closestCategory);
            return products.stream()
                    .map(productMapper::productToProductModel)
                    .toList();
        }

        return Collections.emptyList();
    }

    // Find the best matching category using both Levenshtein Distance and Partial Match
    private ProductCategory getBestMatchingCategory(String inputCategory) {
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        ProductCategory bestMatch = null;
        int minDistance = Integer.MAX_VALUE;

        for (ProductCategory category : ProductCategory.values()) {
            String categoryName = category.name();

            if (categoryName.replace("_", "").contains(inputCategory.replace("_", ""))) {
                return category;
            }

            int distance = levenshtein.apply(categoryName, inputCategory);
            if (distance < minDistance && distance <= 3) {
                minDistance = distance;
                bestMatch = category;
            }
        }
        return bestMatch;
    }


    // Delete Product By ID
    public boolean deleteProductById(Long id) {
        if (productRepository.existsById(id))
        {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }


    // Update Product By ID
    public ProductModel updateProduct(Long id, ProductModel productModel) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        productMapper.updateProductModel(productModel,product);
        product.setId(id);

        Product updatedProducts = productRepository.save(product);

        return productMapper.productToProductModel(updatedProducts);
    }


}

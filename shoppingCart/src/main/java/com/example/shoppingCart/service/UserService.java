package com.example.shoppingCart.service;

import com.example.shoppingCart.entity.Role;
import com.example.shoppingCart.entity.UserRole;
import com.example.shoppingCart.mapper.ProductMapper;
import com.example.shoppingCart.mapper.RoleMapper;
import com.example.shoppingCart.model.ProductModel;
import com.example.shoppingCart.model.RoleModel;
import com.example.shoppingCart.model.UserModel;
import com.example.shoppingCart.entity.Product;
import com.example.shoppingCart.entity.User;
import com.example.shoppingCart.enums.ProductCategory;
import com.example.shoppingCart.mapper.UserMapper;
import com.example.shoppingCart.repository.ProductRepository;
import com.example.shoppingCart.repository.RoleRepository;
import com.example.shoppingCart.repository.UserRepository;
import com.example.shoppingCart.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final UserRoleRepository userRoleRepository;

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

//    public UserModel addCustomer(UserModel userModel){
//        User addUser = userMapper.userModelToUser(userModel);
//        addUser = userRepository.save(addUser);
//        UserModel returnToModel = userMapper.userToUserModel(addUser);
//        return returnToModel;
//    }


    @Transactional
    public UserModel addCustomer(UserModel userModel) {

        User addUser = userMapper.userModelToUser(userModel); // Converted to User Entity

        // Save user to get ID
        addUser = userRepository.save(addUser);

        // Assign roles
        List<Long> roleIdsFromModel = userModel.getRoles().stream().map(r -> r.getRoleId()).toList();

        // Finding all matching Roles From DB
        List<Role> roleInDb = roleRepository.findAllByRoleIdIn(roleIdsFromModel);

        // Extract Roles Id that exists in Database
        List<Long> roleIdsInDb = roleInDb.stream().map(r -> r.getRoleId()).toList();

        List<Long> invalidRoles = new ArrayList<>();

        for(Long roleId : roleIdsFromModel){
            if(!roleIdsInDb.contains(roleId)){
                invalidRoles.add(roleId);
            }
        }

        if(!invalidRoles.isEmpty()){
            throw new IllegalArgumentException("Invalid role ID: " + invalidRoles + ". Allowed role IDs are 1, 2, and 3.");
        }

        List<Role> saveRoles = roleInDb.stream().filter(r -> roleIdsFromModel.contains(r.getRoleId())).toList();

        for(Role role : saveRoles){
            UserRole userRole = new UserRole();
            userRole.setUser(addUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);
        }

        // Fetch user roles from DB (userRoleRepository)
        List<UserRole> userRolesList = userRoleRepository.findByUser(addUser);

        // Extract roles from userRolesList
        List<Role> rolesList = userRolesList.stream()
                .map(UserRole::getRole)
                .toList();

        // Convert to RolesModel for response
        List<RoleModel> rolesModelList = roleMapper.rolesToRolesModel(rolesList);

        // Prepare return model
        UserModel returnToModel = userMapper.userToUserModel(addUser);

        // Set roles in returned UserModel
        returnToModel.setRoles(rolesModelList);

        return returnToModel;



//        // Fetch user roles from DB
//        List<UserRoles> userRolesList = userRolesRepository.findByUser(addUser);
//
//        // Extract roles from userRolesList
//        List<Roles> rolesList = userRolesList.stream()
//                .map(UserRoles::getRoles)
//                .toList();
//
//        // Convert to RolesModel for response
//        List<RolesModel> rolesModelList = rolesMapper.rolesToRolesModel(rolesList);
//
//        // Prepare return model
//        UserModel returnToModel = userMapper.userToUserModel(addUser);
//        returnToModel.setRoles(rolesModelList);

    }

    // Deleting the user By Id
    public String deleteUserById(Long id) {
        Optional<User> userModel = userRepository.findById(id);
        if (userModel.isPresent()) {
            userRepository.deleteById(id);
            return "User deleted successfully.";
        } else {
            return "User not found.";
        }
    }

//    public UserModel updateUser(Long id, UserModel updatedUserModel) {
//        return userRepository.findById(id).map(existingUser -> {
//            // Selectively update fields if not null
//            if (updatedUserModel.getFirstName() != null) existingUser.setFirstName(updatedUserModel.getFirstName());
//            if (updatedUserModel.getLastName() != null) existingUser.setLastName(updatedUserModel.getLastName());
//            if (updatedUserModel.getEmail() != null) existingUser.setEmail(updatedUserModel.getEmail());
//            if (updatedUserModel.getPassword() != null) existingUser.setPassword(updatedUserModel.getPassword());
//            if (updatedUserModel.getPhoneNumber() != null) existingUser.setPhoneNumber(updatedUserModel.getPhoneNumber());
//            if (updatedUserModel.getAddress() != null) existingUser.setAddress(updatedUserModel.getAddress());
//            if (updatedUserModel.getDob() != null) existingUser.setDob(updatedUserModel.getDob());
//            // Save and convert back to DTO
//            User updatedUser = userRepository.save(existingUser);
//            return userMapper.userToUserModel(updatedUser);
//        }).orElse(null);
//    }
// Or

    public UserModel updateuser(Long id, UserModel userModel){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        userMapper.updateUserModel(userModel,user);
        user.setUserId(id);

        User updatedUsers = userRepository.save(user);

        return userMapper.userToUserModel(updatedUsers);

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

    @Autowired
    private ProductMapper productMapper;

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




}


//        if (userModel.getRoles() != null && !userModel.getRoles().isEmpty()) {

//            for (RolesModel roleModel : userModel.getRoles()) {
//                Long roleId = roleModel.getRoleId();
//                // Long roleIdsInDb = rolesRepository.findAllById(roleId);
//                if (!(roleId == 1 || roleId == 2 || roleId == 3)) {
//                    throw new IllegalArgumentException("Invalid role ID: " + roleId + ". Allowed role IDs are 1, 2, and 3.");
//                }
//
//                Roles roleEntity = rolesRepository.findById(roleId)
//                        .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
//
//                UserRoles userRole = new UserRoles();
//                userRole.setUser(addUser);
//                userRole.setRoles(roleEntity);
//                userRolesRepository.save(userRole);
//            }
//        }

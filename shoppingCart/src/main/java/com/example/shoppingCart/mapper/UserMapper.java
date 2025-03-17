package com.example.shoppingCart.mapper;

import com.example.shoppingCart.model.UserModel;
import com.example.shoppingCart.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // "spring" integrates with Spring DI
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target="firstName", source="firstName")
    @Mapping(target="lastName", source="lastName")
    @Mapping(target="email", source="email")
    @Mapping(target="phoneNumber", source="phoneNumber")
    @Mapping(target="address", source="address")
    @Mapping(target="password", source="password")
    @Mapping(target="dob", source="dob")
    UserModel userToUserModel(User user);


//    @Mapping(target="firstName", source="firstName")
//    @Mapping(target="lastName", source="lastName")
//    @Mapping(target="email", source="email")
//    @Mapping(target="phoneNumber", source="phoneNumber")
//    @Mapping(target="password", source="password")
//    @Mapping(target="address", source="address")
//    @Mapping(target="dob", source="dob")
    @Mapping(target="userRoles", ignore = true)
    User userModelToUser(UserModel userModel);

    User updateUserModel(UserModel userModel, @MappingTarget User user);
}

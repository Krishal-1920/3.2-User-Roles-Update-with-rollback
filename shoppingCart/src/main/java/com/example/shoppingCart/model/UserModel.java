package com.example.shoppingCart.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long userId;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String password;
    private LocalDate dob;

    private List<RoleModel> roles;

}

package com.example.tvDispatcher.model;

import lombok.Data;

@Data
public class UserCreateRequest {

    private String name;
    private String surname;
    private String middleName;
    private String email;
    private String password;
    private String gender;
}

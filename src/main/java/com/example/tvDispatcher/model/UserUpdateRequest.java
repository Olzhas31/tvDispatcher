package com.example.tvDispatcher.model;

import com.example.tvDispatcher.entity.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {

    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private Long departmentId;
    private String email;
    private String gender;
    private String phoneNumber;
    private LocalDate birthday;
    private String address;
    private String aboutInformation;
    private String role;
}

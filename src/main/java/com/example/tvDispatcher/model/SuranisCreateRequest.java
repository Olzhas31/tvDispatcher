package com.example.tvDispatcher.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SuranisCreateRequest {

    private String name;
    private String title;
    private String address;
    private String description;
    private String email;
    private String phoneNumber;
    private LocalDateTime meetingTime;

}

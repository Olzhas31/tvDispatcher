package com.example.tvDispatcher.model;

import lombok.Data;

@Data
public class AddUserToSuranisRequest {

    private Long userId;
    private Long suranisId;
}

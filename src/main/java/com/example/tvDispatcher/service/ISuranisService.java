package com.example.tvDispatcher.service;

import com.example.tvDispatcher.entity.Suranis;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.entity.UsersSuranistar;
import com.example.tvDispatcher.model.AddUserToSuranisRequest;
import com.example.tvDispatcher.model.SuranisCreateRequest;

import java.util.List;

public interface ISuranisService {
    void save(SuranisCreateRequest request);

    List<Suranis> getNewSuranistar();

    Suranis getById(Long id);

    void addUserToSuranis(AddUserToSuranisRequest request);

    void approveByDispatcher(Long suranisId);

    List<UsersSuranistar> getActiveSuranistarByUser(User user);

    void approveByEmployee(User user, Long suranisId);
}

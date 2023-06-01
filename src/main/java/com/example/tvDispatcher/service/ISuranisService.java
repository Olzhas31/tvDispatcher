package com.example.tvDispatcher.service;

import com.example.tvDispatcher.entity.Suranis;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.entity.UsersSuranistar;
import com.example.tvDispatcher.model.AddUserToSuranisRequest;
import com.example.tvDispatcher.model.SuranisCreateRequest;
import com.example.tvDispatcher.model.WorkItem;

import java.util.List;

public interface ISuranisService {
    void save(SuranisCreateRequest request);

    List<Suranis> getNewSuranistar();

    Suranis getById(Long id);

    void addUserToSuranis(AddUserToSuranisRequest request);

    void approveByDispatcher(Long suranisId);

    List<UsersSuranistar> getActiveSuranistarByUser(User user);

    void approveByEmployee(User user, Long suranisId);

    List<Suranis> getArchive();

    void approveByManager(User user, Long suranisId, Long userId);

    List<Suranis> getDispatcherApprovedSuranistar();

    List<Suranis> getEmployeeApprovedSuranistar();

    List<Suranis> getManagerApprovedSuranistar();

    void toProcess(Long id);

    List<Suranis> getInProcessSuranistar();

    List<Suranis> getDepartmentSuranistar(User user);

    List<WorkItem> getCalendarByDay(User user);

    void toArchive(Long id);
}

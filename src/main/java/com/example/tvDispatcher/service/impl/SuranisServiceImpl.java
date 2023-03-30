package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.Status;
import com.example.tvDispatcher.entity.Suranis;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.entity.UsersSuranistar;
import com.example.tvDispatcher.model.AddUserToSuranisRequest;
import com.example.tvDispatcher.model.SuranisCreateRequest;
import com.example.tvDispatcher.repository.SuranisRepository;
import com.example.tvDispatcher.repository.UserRepository;
import com.example.tvDispatcher.repository.UsersSuranistarRepository;
import com.example.tvDispatcher.service.ISuranisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SuranisServiceImpl implements ISuranisService {

    private final SuranisRepository suranisRepository;
    private final UserRepository userRepository;
    private final UsersSuranistarRepository usersSuranistarRepository;

    @Override
    public void save(SuranisCreateRequest request) {
        Suranis suranis = Suranis.builder()
                .name(request.getName())
                .address(request.getAddress())
                .description(request.getDescription())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .status(Status.ZHANA_QURILGAN)
                .meetingTime(request.getMeetingTime())
                .createdTime(LocalDateTime.now())
                .title(request.getTitle())
                .build();

        suranisRepository.save(suranis);
    }

    @Override
    public List<Suranis> getNewSuranistar() {
        return suranisRepository.findAll()
                .stream().filter(suranis -> suranis.getStatus().equals(Status.ZHANA_QURILGAN))
                .toList();
    }

    @Override
    public Suranis getById(Long id) {
        return suranisRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void addUserToSuranis(AddUserToSuranisRequest request) {
        Suranis suranis = suranisRepository.findById(request.getSuranisId())
                .orElseThrow(RuntimeException::new);
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(RuntimeException::new);

        UsersSuranistar usersSuranistar = UsersSuranistar.builder()
                .createdTime(LocalDateTime.now())
                .suranis(suranis)
                .user(user)
                .build();
        if (usersSuranistarRepository.existsByUserAndSuranis(user, suranis)) {
            throw new RuntimeException("already added");
        }
        usersSuranistarRepository.save(usersSuranistar);
    }

    @Override
    public void approveByDispatcher(Long suranisId) {
        Suranis suranis = suranisRepository.findById(suranisId)
                .orElseThrow(RuntimeException::new);
        suranis.setStatus(Status.DISPATCHER_QABIDADI);
        suranisRepository.save(suranis);
    }

    @Override
    public List<UsersSuranistar> getActiveSuranistarByUser(User user) {
        return usersSuranistarRepository.findByUser(user)
                .stream()
                .filter(entity -> entity.getSuranis().getStatus().equals(Status.DISPATCHER_QABIDADI))
                .collect(Collectors.toList());
    }

    @Override
    public void approveByEmployee(User user, Long suranisId) {
        Suranis suranis = suranisRepository.findById(suranisId)
                .orElseThrow(RuntimeException::new);
        UsersSuranistar usersSuranis = usersSuranistarRepository
                .findByUserAndSuranis(user, suranis);

        usersSuranis.setEmployeeApproveTime(LocalDateTime.now());
        usersSuranis.setEmployeeApproved(true);

        boolean isAllEmployeeApprove = isAllEmployeeApprove(suranis);
        if (isAllEmployeeApprove) {
            suranis.setStatus(Status.JAUAPTILAR_QABILDADI);
            suranisRepository.save(suranis);
        }
        usersSuranistarRepository.save(usersSuranis);
    }

    private boolean isAllEmployeeApprove(Suranis suranis) {
        for (UsersSuranistar e: suranis.getEmployees()) {
         if (e.getEmployeeApproved() == null) {
             return false;
         } else if (e.getEmployeeApproved() == false) {
             return false;
         }
        }
        return true;
    }
}

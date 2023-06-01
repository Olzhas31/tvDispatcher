package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.*;
import com.example.tvDispatcher.model.AddUserToSuranisRequest;
import com.example.tvDispatcher.model.SuranisCreateRequest;
import com.example.tvDispatcher.model.WorkItem;
import com.example.tvDispatcher.repository.*;
import com.example.tvDispatcher.service.INotificationService;
import com.example.tvDispatcher.service.ISuranisService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SuranisServiceImpl implements ISuranisService {

    private final SuranisRepository suranisRepository;
    private final UserRepository userRepository;
    private final UsersSuranistarRepository usersSuranistarRepository;
    private final INotificationService notificationService;
    private final NotificationRepository notificationRepository;
    private final RoleRepository roleRepository;

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

        suranis = suranisRepository.save(suranis);
        Role role = roleRepository.findByName("DISPATCHER");
        List<User> dispatchers = userRepository.findAllByRole(role);
        for (User user : dispatchers) {
            Notification notification = Notification.builder()
                    .content("Жаңа сұраныс құрылды")
                    .createdTime(LocalDateTime.now())
                    .isRead(false)
                    .user(user)
                    .build();
            notificationRepository.save(notification);
        }
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

        Notification notification = Notification.builder()
                .content("Жаңа сұраныс тағайындалды")
                .createdTime(LocalDateTime.now())
                .isRead(false)
                .user(user)
                .build();
        notificationRepository.save(notification);
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
                .filter(entity -> {
                    return entity.getSuranis().getStatus().equals(Status.DISPATCHER_QABIDADI) ||
                            entity.getSuranis().getStatus().equals(Status.JAUAPTILAR_QABILDADI) ||
                            entity.getSuranis().getStatus().equals(Status.MANAGER_QABILDADI) ||
                            entity.getSuranis().getStatus().equals(Status.PROCESSTE);
                })
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

        Department department = user.getDepartment();
        User manager = null;
        if (department != null) {
            manager = department.getManager();
        }
        if (manager != null) {
            Notification notification = Notification.builder()
                    .content("Қызметкер сұранысты қабылдады")
                    .createdTime(LocalDateTime.now())
                    .isRead(false)
                    .user(manager)
                    .build();
            notificationRepository.save(notification);
        }
    }

    @Override
    public List<Suranis> getArchive() {
        return suranisRepository.findAll()
                .stream()
                .filter(suranis -> suranis.getStatus().equals(Status.ZHABILDI) ||
                        suranis.getStatus().equals(Status.UAQITI_OTIP_KETTI))
                .toList();
    }

    @Override
    public void approveByManager(User manager, Long suranisId, Long workerId) {
        Suranis suranis = suranisRepository.findById(suranisId)
                .orElseThrow(RuntimeException::new);
        User worker = userRepository.findById(workerId)
                .orElseThrow(RuntimeException::new);
        UsersSuranistar usersSuranis = usersSuranistarRepository
                .findByUserAndSuranis(worker, suranis);

        if (usersSuranis.getManagerApproved() == null) {
            usersSuranis.setManagerApproved(true);
            usersSuranis.setManagerApproveTime(LocalDateTime.now());
        } else if (usersSuranis.getManagerApproved() == false) {
            usersSuranis.setManagerApproved(true);
            usersSuranis.setManagerApproveTime(LocalDateTime.now());
        }

        Notification notification = Notification.builder()
                .content("Менеджер сұранысты қабылдады")
                .createdTime(LocalDateTime.now())
                .isRead(false)
                .user(worker)
                .build();
        notificationRepository.save(notification);

        boolean isAllManagerApprove = isAllManagersApprove(suranis);
        if (isAllManagerApprove) {
            suranis.setStatus(Status.MANAGER_QABILDADI);
            suranisRepository.save(suranis);

            Role role = roleRepository.findByName("DISPATCHER");
            List<User> dispatchers = userRepository.findAllByRole(role);
            for (User user : dispatchers) {
                Notification notification2 = Notification.builder()
                        .content("Барлық менеджер сұранысты қабылдады")
                        .createdTime(LocalDateTime.now())
                        .isRead(false)
                        .user(user)
                        .build();
                notificationRepository.save(notification2);
            }
        }

        usersSuranistarRepository.save(usersSuranis);
    }

    @Override
    public List<Suranis> getDispatcherApprovedSuranistar() {
        return suranisRepository.findAll()
                .stream()
                .filter(suranis -> suranis.getStatus().equals(Status.DISPATCHER_QABIDADI))
                .toList();
    }

    @Override
    public List<Suranis> getEmployeeApprovedSuranistar() {
        return suranisRepository.findAll()
                .stream()
                .filter(suranis -> suranis.getStatus().equals(Status.JAUAPTILAR_QABILDADI))
                .toList();
    }

    @Override
    public List<Suranis> getManagerApprovedSuranistar() {
        return suranisRepository.findAll()
                .stream()
                .filter(suranis -> suranis.getStatus().equals(Status.MANAGER_QABILDADI))
                .toList();
    }

    @Override
    public void toProcess(Long id) {
        Suranis suranis = suranisRepository.findById(id).orElseThrow(RuntimeException::new);
        suranis.setStatus(Status.PROCESSTE);
        suranisRepository.save(suranis);

        var suranistar = suranis.getEmployees();
        List<User> users = new ArrayList<>();

        suranistar.stream().forEach(item -> {
            if (item.getEmployeeApproved() && item.getManagerApproved()) {
                users.add(item.getUser());

                Department department = item.getUser().getDepartment();
                User manager = null;
                if (department != null) {
                    manager = department.getManager();
                }
                if (manager != null) {
                    users.add(manager);
                }
            }
        });
        for (User user : users) {
            Notification notification2 = Notification.builder()
                    .content("Сұраныс орындауға жіберілді")
                    .createdTime(LocalDateTime.now())
                    .isRead(false)
                    .user(user)
                    .build();
            notificationRepository.save(notification2);
        }
    }

    @Override
    public List<Suranis> getInProcessSuranistar() {
        return suranisRepository.findAll()
                .stream()
                .filter(suranis -> suranis.getStatus().equals(Status.PROCESSTE))
                .toList();
    }

    @Override
    public List<Suranis> getDepartmentSuranistar(User manager) {
        List<Suranis> result = new ArrayList<>();
        Department department = manager.getDepartmentOfManager();

        if (department != null) {
            List<User> users = userRepository.findAllByDepartment(department);
            for (User user : users) {
                var active = getActiveSuranistarByUser(user);
                active.forEach(
                        usersSuranistar -> result.add(usersSuranistar.getSuranis())
                );
            }
        }
        return result.stream().distinct().toList();
    }

    @Override
    public List<WorkItem> getCalendarByDay(User user) {
        List<WorkItem> response = new ArrayList<>();
        List<Suranis> suranistar = suranisRepository.findAllByStatus(Status.PROCESSTE);
        for (Suranis suranis : suranistar) {
            if (suranis.getMeetingTime().getDayOfYear() == LocalDate.now().getDayOfYear()) {
                for (UsersSuranistar item : suranis.getEmployees()) {
                    if (item.getUser().equals(user) && item.getManagerApproved() && item.getEmployeeApproved()) {
                        response.add(WorkItem.builder()
                                .suranisId(suranis.getId())
                                .dateTime(suranis.getMeetingTime())
                                .title(suranis.getTitle())
                                .address(suranis.getAddress())
                                .build());
                    }
                }
            }
        }
        return response.stream()
                .sorted(Comparator.comparing(WorkItem::getDateTime))
                .distinct().toList();
    }

    @Override
    public void toArchive(Long id) {
        Suranis suranis = suranisRepository.findById(id).orElseThrow(RuntimeException::new);
        suranis.setStatus(Status.ZHABILDI);
        suranisRepository.save(suranis);
    }

    private boolean isAllManagersApprove(Suranis suranis) {
        for (UsersSuranistar item : suranis.getEmployees()) {
            if (item.getManagerApproved() == null) {
                return false;
            } else if (item.getManagerApproved() == false) {
                return false;
            }
        }
        return true;
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

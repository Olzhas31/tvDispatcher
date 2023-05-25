package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.*;
import com.example.tvDispatcher.exception.UserBlockedException;
import com.example.tvDispatcher.exception.UserNotEnabledException;
import com.example.tvDispatcher.model.UserCreateRequest;
import com.example.tvDispatcher.model.UserUpdateRequest;
import com.example.tvDispatcher.repository.DepartmentRepository;
import com.example.tvDispatcher.repository.RoleRepository;
import com.example.tvDispatcher.repository.UserRepository;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
        if (user.getLocked()) {
            throw new UserBlockedException("blocked");
        }
        if (!user.getEnabled()) {
            System.out.println("not enabled");
            throw new UserNotEnabledException("notEnabled");
        }
        return user;
    }

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public void updateUser(UserUpdateRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(RuntimeException::new);
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(RuntimeException::new);
        Role role = roleRepository.findByName(request.getRole());

        if (userRepository.existsByEmail(request.getEmail())) {
            if (!user.getEmail().equals(request.getEmail())) {
                throw new RuntimeException("email is already exist");
            }
        }

        user.setRole(role);
        user.setEmail(request.getEmail());
        user.setDepartment(department);

        EmployeeInfo info = user.getInfo();

        info.setName(request.getName());
        info.setSurname(request.getSurname());
        info.setMiddleName(request.getMiddleName());
        info.setGender(Gender.valueOf(request.getGender()));
        info.setPhoneNumber(request.getPhoneNumber());
        info.setAboutInformation(request.getAboutInformation());
        info.setBirthday(request.getBirthday());
        info.setAddress(request.getAddress());

        userRepository.save(user);
    }

    @Override
    public void save(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("email is already exist");
        }
        Role role = roleRepository.findByName("USER");

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .enabled(false)
                .locked(false)
                .build();
        EmployeeInfo employeeInfo = EmployeeInfo.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .middleName(request.getMiddleName())
                .gender(Gender.valueOf(request.getGender()))
                .user(user)
                .registerDay(LocalDate.now())
                .build();
        user.setInfo(employeeInfo);

        userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        // todo тексеру
        // TODO enable false. blocked true болса қоспау
        return userRepository.findAll()
                .stream().filter(user -> {
                    return user.isEnabled() && user.isAccountNonLocked() && !user.getRole().getName().equals("ADMIN");
                })
                .toList();
    }

    @Override
    public List<User> getUsers(boolean enabled) {
        Predicate<User> userPredicate = user -> !user.getEnabled();
        if (enabled) {
            userPredicate = User::getEnabled;
        }
        return userRepository.findAll()
                .stream()
                .filter(user -> !user.getRole().getName().equals("ADMIN"))
                .filter(userPredicate)
                .toList();
    }

    @Override
    public void enableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void updateLock(Long id, boolean blocked) {
        User user = userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        user.setLocked(blocked);
        userRepository.save(user);
    }
}

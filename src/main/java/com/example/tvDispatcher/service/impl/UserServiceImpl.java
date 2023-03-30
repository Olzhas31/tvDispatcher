package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.*;
import com.example.tvDispatcher.model.UserCreateRequest;
import com.example.tvDispatcher.model.UserUpdateRequest;
import com.example.tvDispatcher.repository.DepartmentRepository;
import com.example.tvDispatcher.repository.RoleRepository;
import com.example.tvDispatcher.repository.UserRepository;
import com.example.tvDispatcher.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email not found" + email));
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
        return userRepository.findAll()
                .stream().filter(user -> !user.getRole().getName().equals("ADMIN"))
                .toList();
    }
}

package com.example.tvDispatcher.config;

import com.example.tvDispatcher.entity.*;
import com.example.tvDispatcher.repository.DepartmentRepository;
import com.example.tvDispatcher.repository.RoleRepository;
import com.example.tvDispatcher.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class InitData implements CommandLineRunner {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final Data data;

    private final String password = "1";

    @Override
    public void run(String... args) throws Exception {
        initDepartments(data.getDepartments());
        initRoles(data.getRoles());
        initAdmin();
        initUsers(data.getUsers());
    }

    private void initRoles(String[] roles) {
        for (String role: roles) {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(Role.builder()
                        .name(role)
                        .build());
            }
        }
    }

    private void initDepartments(String[][] departments) {
        for (String[] department :departments) {
            if (!departmentRepository.existsByName(department[0])) {
                Department departmentEntity = Department.builder()
                        .name(department[0])
                        .description(department[1])
                        .build();
                departmentRepository.save(departmentEntity);
            }
        }
    }
    // {email, role, department_name, name, surname, middle_name, gender, phone_number, about_information}
    private void initUsers(String[][] users) {
        for (String[] data: users) {
            if (!userRepository.existsByEmail(data[0])) {
                Department department = departmentRepository.findByName(data[2])
                        .orElse(null);
                Role role = roleRepository.findByName(data[1]);

                User user = User.builder()
                        .email(data[0])
                        .password(passwordEncoder.encode(password))
                        .role(role)
                        .department(department)
                        .build();
                EmployeeInfo employeeInfo = EmployeeInfo.builder()
                        .name(data[3])
                        .surname(data[4])
                        .middleName(data[5])
                        .gender(Gender.valueOf(data[6]))
                        .phoneNumber(data[7])
                        .aboutInformation(data[8])
                        .user(user)
                        .registerDay(LocalDate.now())
                        .build();
                user.setInfo(employeeInfo);

                userRepository.save(user);
            }
        }
    }

    private void initAdmin() {
        User admin = User.builder()
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("100"))
                .role(roleRepository.findByName("ADMIN"))
                .build();
        if (!userRepository.existsByEmail(admin.getUsername())) {
            userRepository.save(admin);
        }
    }

}

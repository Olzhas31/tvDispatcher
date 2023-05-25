package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.Department;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.repository.DepartmentRepository;
import com.example.tvDispatcher.repository.UserRepository;
import com.example.tvDispatcher.service.IDepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    @Override
    public Department create(String name, String description) {
        Department department = Department.builder()
                .description(description)
                .name(name)
                .build();
        return departmentRepository.save(department);
    }

    @Override
    public void update(Long departmentId, String name, String description, Long managerId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(RuntimeException::new);
        if (managerId != -1) {
            User user = userRepository.findById(managerId)
                    .orElseThrow(RuntimeException::new);
            department.setManager(user);
        } else {
            department.setManager(null);
        }

        department.setName(name);
        department.setDescription(description);
        departmentRepository.save(department);
    }
}

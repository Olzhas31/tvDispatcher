package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.Department;
import com.example.tvDispatcher.repository.DepartmentRepository;
import com.example.tvDispatcher.service.IDepartmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements IDepartmentService {

    private final DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }
}

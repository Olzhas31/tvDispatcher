package com.example.tvDispatcher.service;

import com.example.tvDispatcher.entity.Department;

import java.util.List;

public interface IDepartmentService {
    List<Department> getAll();

    Department getById(Long id);

    Department create(String name, String description);

    void update(Long departmentId, String name, String description, Long managerId);
}

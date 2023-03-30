package com.example.tvDispatcher.service;

import com.example.tvDispatcher.entity.Role;

import java.util.List;

public interface IRoleService {
    List<Role> findAllWithoutAdmin();
}

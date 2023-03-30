package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.Role;
import com.example.tvDispatcher.repository.RoleRepository;
import com.example.tvDispatcher.service.IRoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAllWithoutAdmin() {
        return roleRepository.findAll()
                .stream().filter(role -> !role.getName().equals("ADMIN"))
                .toList();
    }
}

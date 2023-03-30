package com.example.tvDispatcher.service;

import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.model.UserCreateRequest;
import com.example.tvDispatcher.model.UserUpdateRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {

    User getById(Long id);

    void updateUser(UserUpdateRequest request);

    void save(UserCreateRequest request);

    List<User> getUsers();
}

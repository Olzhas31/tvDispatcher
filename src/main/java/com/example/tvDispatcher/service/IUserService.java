package com.example.tvDispatcher.service;

import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.model.UserCreateRequest;
import com.example.tvDispatcher.model.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends UserDetailsService {

    User getById(Long id);

    void updateUser(UserUpdateRequest request);

    void save(UserCreateRequest request);

    List<User> getUsers();

    List<User> getUsers(boolean enabled);

    void enableUser(Long id);

    void updateLock(Long id, boolean blocked);
}

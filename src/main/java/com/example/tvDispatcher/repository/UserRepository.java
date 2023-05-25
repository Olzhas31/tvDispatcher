package com.example.tvDispatcher.repository;

import com.example.tvDispatcher.entity.Department;
import com.example.tvDispatcher.entity.Role;
import com.example.tvDispatcher.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    List<User> findAllByRole(Role role);

    List<User> findAllByDepartment(Department department);
}

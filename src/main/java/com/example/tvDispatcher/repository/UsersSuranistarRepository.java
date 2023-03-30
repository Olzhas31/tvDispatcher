package com.example.tvDispatcher.repository;

import com.example.tvDispatcher.entity.Suranis;
import com.example.tvDispatcher.entity.User;
import com.example.tvDispatcher.entity.UsersSuranistar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersSuranistarRepository extends JpaRepository<UsersSuranistar, Long> {

    boolean existsByUserAndSuranis(User user, Suranis suranis);

    List<UsersSuranistar> findByUser(User user);

    UsersSuranistar findByUserAndSuranis(User user, Suranis suranis);
}
package com.example.tvDispatcher.repository;

import com.example.tvDispatcher.entity.Status;
import com.example.tvDispatcher.entity.Suranis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuranisRepository extends JpaRepository<Suranis, Long> {

    List<Suranis> findAllByStatus(Status status);
}
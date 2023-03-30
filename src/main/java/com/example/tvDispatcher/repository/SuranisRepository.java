package com.example.tvDispatcher.repository;

import com.example.tvDispatcher.entity.Suranis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuranisRepository extends JpaRepository<Suranis, Long> {
}
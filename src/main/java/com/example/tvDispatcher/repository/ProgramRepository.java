package com.example.tvDispatcher.repository;

import com.example.tvDispatcher.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

    List<Program> findAllByDateTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
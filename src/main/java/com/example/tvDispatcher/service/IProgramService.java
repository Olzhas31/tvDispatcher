package com.example.tvDispatcher.service;

import com.example.tvDispatcher.entity.Program;

import java.time.LocalDateTime;
import java.util.List;

public interface IProgramService {
    List<Program> getProgramsByDate(Integer date);

    void createProgram(String title, LocalDateTime dateTime);

    void deleteById(Long id);

    void updateById(Long id, String title, LocalDateTime dateTime);
}

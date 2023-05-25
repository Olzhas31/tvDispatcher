package com.example.tvDispatcher.service.impl;

import com.example.tvDispatcher.entity.Program;
import com.example.tvDispatcher.repository.ProgramRepository;
import com.example.tvDispatcher.service.IProgramService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class ProgramServiceImpl implements IProgramService {

    private final ProgramRepository repository;

    @Override
    public List<Program> getProgramsByDate(Integer dayOfWeek) {
        LocalDate today = LocalDate.now();
        int currentDayOfWeek = today.getDayOfWeek().getValue();
        int diff = currentDayOfWeek - dayOfWeek;
        LocalDate date = today.minusDays(diff);
        return repository.findAllByDateTimeBetween(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX))
                .stream()
                .sorted((Comparator.comparing(Program::getDateTime)))
                .toList();
    }

    @Override
    public void createProgram(String title, LocalDateTime dateTime) {
        repository.save(Program.builder()
                        .title(title)
                        .dateTime(dateTime).build());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void updateById(Long id, String title, LocalDateTime dateTime) {
        Program program = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("program not found"));
        program.setTitle(title);
        program.setDateTime(dateTime);
        repository.save(program);
    }
}

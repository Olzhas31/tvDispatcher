package com.example.tvDispatcher.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WorkItem {

    private Long suranisId;
    private LocalDateTime dateTime;
    private String title;
    private String address;
}

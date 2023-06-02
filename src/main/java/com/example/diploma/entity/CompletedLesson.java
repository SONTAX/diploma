package com.example.diploma.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "completed_lessons")
@Getter
@Setter
@EqualsAndHashCode
public class CompletedLesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long lessonId;

    private LocalDateTime completionDate;

    private Integer grade;
}

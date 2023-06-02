package com.example.diploma.repo;

import com.example.diploma.entity.CompletedLesson;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompletedLessonRepository extends CrudRepository<CompletedLesson, Long> {

    List<CompletedLesson> findByUserId(Long userId);

    List<CompletedLesson> findByLessonId(Long lessonId);

}

package com.example.diploma.repo;

import com.example.diploma.entity.Lesson;
import com.example.diploma.entity.LessonType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {

    List<Lesson> findByCourseId(Long courseId);

    List<Lesson> findByLessonType(LessonType lessonType);

    List<Lesson> findByTitleContaining(String keyword);

    List<Lesson> findAll();

}

package com.example.diploma.repo;

import com.example.diploma.entity.Course;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

    List<Course> findByTitleContaining(String keyword);

    List<Course> findByTeacherId(Long teacherId);

    List<Course> findAll();

}

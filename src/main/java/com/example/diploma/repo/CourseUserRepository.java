package com.example.diploma.repo;

import com.example.diploma.entity.CourseUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseUserRepository extends CrudRepository<CourseUser, Long> {
    void deleteByUserIdAndCourseId(Long userId, Long courseId);

    List<CourseUser> findAllByCourseId(Long courseId);
}
package com.klu.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByEducatorEmailIgnoreCase(String educatorEmail);
}

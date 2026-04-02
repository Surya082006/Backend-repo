package com.klu.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByCourseId(Long courseId);
}
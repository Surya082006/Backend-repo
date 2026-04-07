package com.klu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klu.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // ✅ FIXED (correct field name)
    List<Enrollment> findByUserEmail(String userEmail);

    // ✅ For finding all students in a course
    List<Enrollment> findByCourseId(Long courseId);

    // ✅ For unenroll API
    void deleteByUserEmailAndCourseId(String userEmail, Long courseId);

    // ✅ For exact match checking 
    java.util.Optional<Enrollment> findByUserEmailAndCourseId(String userEmail, Long courseId);
}
package com.klu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.klu.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // ✅ FIXED (correct field name)
    List<Enrollment> findByUserEmail(String userEmail);

    // ✅ For unenroll API
    void deleteByUserEmailAndCourseId(String userEmail, Long courseId);
}
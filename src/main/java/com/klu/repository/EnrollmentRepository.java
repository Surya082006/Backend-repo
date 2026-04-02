package com.klu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentEmail(String email);
}
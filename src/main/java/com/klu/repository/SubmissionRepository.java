package com.klu.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.klu.model.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByAssignmentId(Long assignmentId);
}
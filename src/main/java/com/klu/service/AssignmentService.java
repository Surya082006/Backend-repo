package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Assignment;
import com.klu.model.Submission;
import com.klu.repository.AssignmentRepository;
import com.klu.repository.SubmissionRepository;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepo;

    @Autowired
    private SubmissionRepository submissionRepo;

    // 👨‍🏫 Create assignment
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepo.save(assignment);
    }

    // 👨‍🎓 View assignments by course
    public List<Assignment> getAssignments(Long courseId) {
        return assignmentRepo.findByCourseId(courseId);
    }

    // 👨‍🎓 Submit assignment
    public Submission submit(Submission submission) {
        return submissionRepo.save(submission);
    }

    // 👨‍🏫 View submissions for assignment
    public List<Submission> getSubmissions(Long assignmentId) {
        return submissionRepo.findByAssignmentId(assignmentId);
    }

    // 🔥 NEW → Grade submission (IMPORTANT)
    public Submission gradeSubmission(Long id, Integer grade) {

        Submission sub = submissionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        sub.setGrade(grade);

        return submissionRepo.save(sub);
    }
}
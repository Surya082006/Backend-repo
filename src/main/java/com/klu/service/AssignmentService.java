package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Assignment;
import com.klu.model.Enrollment;
import com.klu.model.Submission;
import com.klu.repository.AssignmentRepository;
import com.klu.repository.CourseRepository;
import com.klu.repository.EnrollmentRepository;
import com.klu.repository.SubmissionRepository;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepo;

    @Autowired
    private SubmissionRepository submissionRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private CourseRepository courseRepo;

    // 👨‍🏫 Create assignment
    public Assignment createAssignment(Assignment assignment) {
        Assignment saved = assignmentRepo.save(assignment);

        courseRepo.findById(assignment.getCourseId()).ifPresent(course -> {
            List<Enrollment> enrollments = enrollmentRepo.findByCourseId(course.getId());
            for (Enrollment e : enrollments) {
                String subject = "New Assignment: " + assignment.getTitle() + " in " + course.getTitle();
                String text = "Hello,\n\nA new assignment titled '" + assignment.getTitle() + "' has been posted in your course '" + course.getTitle() + "'.\n\nQuestion: " + assignment.getQuestion() + "\n\nPlease check your dashboard for details.";
                emailService.sendEmail(e.getUserEmail(), subject, text);
            }
        });

        return saved;
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
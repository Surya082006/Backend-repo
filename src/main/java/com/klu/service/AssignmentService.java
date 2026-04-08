package com.klu.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Assignment;
import com.klu.model.Course;
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
        Course course = courseRepo.findById(assignment.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getEducatorEmail().equalsIgnoreCase(assignment.getEducatorEmail())) {
            throw new RuntimeException("You can only create assignments for your own courses");
        }

        Assignment saved = assignmentRepo.save(assignment);

        List<Enrollment> enrollments = enrollmentRepo.findByCourseId(course.getId());
        for (Enrollment e : enrollments) {
            String subject = "New Assignment: " + assignment.getTitle() + " in " + course.getTitle();
            String text = "Hello,\n\nA new assignment titled '" + assignment.getTitle() + "' has been posted in your course '" + course.getTitle() + "'.\n\nQuestion: " + assignment.getQuestion() + "\n\nPlease check your dashboard for details.";
            emailService.sendEmail(e.getUserEmail(), subject, text);
        }

        return saved;
    }

    // 👨‍🎓 View assignments by course
    public List<Assignment> getAssignments(Long courseId, String studentEmail) {
        enrollmentRepo.findByUserEmailIgnoreCaseAndCourseId(studentEmail, courseId)
                .orElseThrow(() -> new RuntimeException("Enroll in the course to access assignments"));

        List<Assignment> assignments = assignmentRepo.findByCourseId(courseId);

        for (Assignment assignment : assignments) {
            submissionRepo.findByAssignmentIdAndStudentEmail(assignment.getId(), studentEmail)
                    .ifPresentOrElse(
                            submission -> {
                                assignment.setSubmitted(true);
                                assignment.setGrade(submission.getGrade());
                            },
                            () -> {
                                assignment.setSubmitted(false);
                                assignment.setGrade(null);
                            });
        }

        return assignments;
    }

    // 👨‍🎓 Submit assignment
    public Submission submit(Submission submission) {
        Assignment assignment = assignmentRepo.findById(submission.getAssignmentId())
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        enrollmentRepo.findByUserEmailIgnoreCaseAndCourseId(submission.getStudentEmail(), assignment.getCourseId())
                .orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        Submission existing = submissionRepo.findByAssignmentIdAndStudentEmail(
                submission.getAssignmentId(),
                submission.getStudentEmail()
        ).orElse(null);

        if (existing != null) {
            existing.setFileUrl(submission.getFileUrl());
            existing.setDescription(submission.getDescription());
            return submissionRepo.save(existing);
        }

        return submissionRepo.save(submission);
    }

    public List<Assignment> getEducatorAssignments(Long courseId, String educatorEmail) {
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getEducatorEmail().equalsIgnoreCase(educatorEmail)) {
            throw new RuntimeException("You can only access assignments for your own courses");
        }

        return assignmentRepo.findByCourseId(courseId);
    }

    // 👨‍🏫 View submissions for assignment
    public List<Submission> getSubmissions(Long assignmentId, String educatorEmail) {
        Assignment assignment = assignmentRepo.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        Course course = courseRepo.findById(assignment.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getEducatorEmail().equalsIgnoreCase(educatorEmail)) {
            throw new RuntimeException("You can only access submissions for your own courses");
        }

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

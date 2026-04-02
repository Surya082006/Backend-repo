package com.klu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.klu.model.Assignment;
import com.klu.model.Submission;
import com.klu.service.AssignmentService;

@RestController
@RequestMapping("/api")
public class AssignmentController {

    @Autowired
    private AssignmentService service;

    // 👨‍🏫 Create assignment
    @PostMapping("/educator/assignment")
    public Assignment create(@RequestBody Assignment assignment, Authentication auth) {
        assignment.setEducatorEmail(auth.getName());
        return service.createAssignment(assignment);
    }

    // 👨‍🎓 View assignments
    @GetMapping("/student/assignments/{courseId}")
    public List<Assignment> getAssignments(@PathVariable Long courseId) {
        return service.getAssignments(courseId);
    }

    // 👨‍🎓 Submit assignment
    @PostMapping("/student/submit")
    public Submission submit(@RequestBody Submission submission, Authentication auth) {
        submission.setStudentEmail(auth.getName());
        return service.submit(submission);
    }

    // 👨‍🏫 View submissions
    @GetMapping("/educator/submissions/{assignmentId}")
    public List<Submission> getSubmissions(@PathVariable Long assignmentId) {
        return service.getSubmissions(assignmentId);
    }
}
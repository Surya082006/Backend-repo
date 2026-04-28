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

    
    @PostMapping("/educator/assignment")
    public Assignment create(@RequestBody Assignment assignment, Authentication auth) {
        assignment.setEducatorEmail(auth.getName());
        return service.createAssignment(assignment);
    }

    @GetMapping("/educator/courses/{courseId}/assignments")
    public List<Assignment> getEducatorAssignments(@PathVariable Long courseId, Authentication auth) {
        return service.getEducatorAssignments(courseId, auth.getName());
    }

    
    @GetMapping("/student/assignments/{courseId}")
    public List<Assignment> getAssignments(@PathVariable Long courseId, Authentication auth) {
        return service.getAssignments(courseId, auth.getName());
    }

    
    @PostMapping("/student/submit")
    public Submission submit(@RequestBody Submission submission, Authentication auth) {
        submission.setStudentEmail(auth.getName());
        return service.submit(submission);
    }

    @GetMapping("/educator/submissions/{assignmentId}")
    public List<Submission> getSubmissions(@PathVariable Long assignmentId, Authentication auth) {
        return service.getSubmissions(assignmentId, auth.getName());
    }

    @PutMapping("/educator/submission/{id}/grade")
    public Submission grade(
            @PathVariable Long id,
            @RequestParam Integer grade) {

        return service.gradeSubmission(id, grade);
    }
}

package com.klu.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.klu.model.Enrollment;
import com.klu.service.EnrollmentService;

@RestController
@RequestMapping("/api")
public class EnrollmentController {

    @Autowired
    private EnrollmentService service;

    // ✅ Enroll
    @PostMapping("/student/enroll/{courseId}")
    public Enrollment enroll(@PathVariable Long courseId, Authentication auth) {
        return service.enroll(auth.getName(), courseId);
    }

    // ❌ Unenroll
    @DeleteMapping("/student/enroll/{courseId}")
    public String unenroll(@PathVariable Long courseId, Authentication auth) {
        service.unenroll(auth.getName(), courseId);
        return "Unenrolled";
    }

    // 👨‍🏫 Students list
    @GetMapping("/educator/students")
    public List<Map<String, Object>> students(Authentication auth) {
        return service.getEducatorStudents(auth.getName());
    }

    @GetMapping("/educator/students/all")
    public List<Map<String, String>> allStudents() {
        return service.getAllStudents();
    }

    @PostMapping("/educator/students/assign")
    public Enrollment assignStudent(@RequestBody Map<String, String> request, Authentication auth) {
        String studentEmail = request.get("studentEmail");
        String courseId = request.get("courseId");

        if (studentEmail == null || studentEmail.isBlank() || courseId == null || courseId.isBlank()) {
            throw new RuntimeException("Student email and course are required");
        }

        return service.assignStudentToCourse(auth.getName(), studentEmail, Long.valueOf(courseId));
    }
}

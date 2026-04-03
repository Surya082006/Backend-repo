package com.klu.controller;

import java.util.List;

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
    public List<Enrollment> students() {
        return service.getAll();
    }
}
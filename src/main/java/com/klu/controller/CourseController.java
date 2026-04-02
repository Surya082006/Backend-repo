package com.klu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.klu.model.Course;
import com.klu.service.CourseService;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService service;

    // 👨‍🏫 EDUCATOR → Create Course
    @PostMapping("/educator/create")
    public Course create(@RequestBody Course course) {
        return service.createCourse(course);
    }

    // 👨‍🎓 STUDENT → Get All Courses
    @GetMapping("/student/courses")
    public List<Course> getAll() {
        return service.getAllCourses();
    }

    // 🔥 NEW → My Courses (IMPORTANT)
    @GetMapping("/student/my-courses")
    public List<Course> myCourses(Authentication auth) {
        return service.getMyCourseDetails(auth.getName());
    }
}
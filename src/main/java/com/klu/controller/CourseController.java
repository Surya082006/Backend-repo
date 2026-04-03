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
    @PostMapping({"/educator/create", "/educator/courses"})
    public Course create(@RequestBody Course course, Authentication auth) {
        return service.createCourse(course, auth.getName());
    }

    // 👨‍🏫 EDUCATOR → Update Course
    @PutMapping("/educator/courses/{courseId}")
    public Course update(@PathVariable Long courseId, @RequestBody Course course, Authentication auth) {
        return service.updateCourse(courseId, course, auth.getName());
    }

    // 👨‍🏫 EDUCATOR → View My Created Courses
    @GetMapping("/educator/courses")
    public List<Course> educatorCourses(Authentication auth) {
        return service.getEducatorCourses(auth.getName());
    }

    // 👨‍🎓 STUDENT → Get All Courses
    @GetMapping("/student/courses")
    public List<Course> getAll() {
        return service.getAllCourses();
    }

    // 👨‍🎓 STUDENT → Get Course Details
    @GetMapping("/student/courses/{courseId}")
    public Course getOne(@PathVariable Long courseId) {
        return service.getCourseById(courseId);
    }

    // 🔥 NEW → My Courses (IMPORTANT)
    @GetMapping("/student/my-courses")
    public List<Course> myCourses(Authentication auth) {
        return service.getMyCourseDetails(auth.getName());
    }
}

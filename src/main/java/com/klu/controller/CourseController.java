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

    // 👨‍🏫 CREATE COURSE
    @PostMapping("/educator/courses")
    public Course create(@RequestBody Course course, Authentication auth) {
        return service.createCourse(course, auth.getName());
    }

    // 👨‍🏫 UPDATE COURSE
    @PutMapping("/educator/courses/{courseId}")
    public Course update(@PathVariable Long courseId,
                         @RequestBody Course course,
                         Authentication auth) {
        return service.updateCourse(courseId, course, auth.getName());
    }

    // 👨‍🏫 GET EDUCATOR COURSES
    @GetMapping("/educator/courses")
    public List<Course> educatorCourses(Authentication auth) {
        return service.getEducatorCourses(auth.getName());
    }

    // 👨‍🎓 GET ALL COURSES
    @GetMapping("/student/courses")
    public List<Course> getAll() {
        return service.getAllCourses();
    }

    // 👨‍🎓 GET COURSE BY ID
    @GetMapping("/student/courses/{courseId}")
    public Course getOne(@PathVariable Long courseId) {
        return service.getCourseById(courseId);
    }

    // 👨‍🎓 MY ENROLLED COURSES
    @GetMapping("/student/my-courses")
    public List<Course> myCourses(Authentication auth) {
        return service.getMyCourseDetails(auth.getName());
    }
}
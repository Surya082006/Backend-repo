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

    // CREATE COURSE
    @PostMapping("/educator/courses")
    public Course create(@RequestBody Course course, Authentication auth) {
        return service.createCourse(course, auth.getName());
    }

    // UPDATE COURSE
    @PutMapping("/educator/courses/{courseId}")
    public Course update(@PathVariable Long courseId,
                         @RequestBody Course course,
                         Authentication auth) {
        return service.updateCourse(courseId, course, auth.getName());
    }

    @DeleteMapping("/educator/courses/{courseId}")
    public String delete(@PathVariable Long courseId, Authentication auth) {
        service.deleteCourse(courseId, auth.getName());
        return "Course deleted successfully";
    }

    //  GET EDUCATOR COURSES
    @GetMapping("/educator/courses")
    public List<Course> educatorCourses(Authentication auth) {
        return service.getEducatorCourses(auth.getName());
    }

    @GetMapping("/educator/courses/{courseId}")
    public Course educatorCourse(@PathVariable Long courseId, Authentication auth) {
        return service.getEducatorCourse(courseId, auth.getName());
    }

    // GET ALL COURSES
    @GetMapping("/student/courses")
    public List<Course> getAll() {
        return service.getAllCourses();
    }

    //  GET COURSE BY ID
    @GetMapping("/student/courses/{courseId}")
    public Course getOne(@PathVariable Long courseId) {
        return service.getCourseById(courseId);
    }

    
    @GetMapping("/student/my-courses")
    public List<Course> myCourses(Authentication auth) {
        return service.getMyCourseDetails(auth.getName());
    }
}

package com.klu.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.klu.model.Course;
import com.klu.repository.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repo;

    public Course createCourse(Course course) {
        return repo.save(course);
    }

    public List<Course> getAllCourses() {
        return repo.findAll();
    }
}
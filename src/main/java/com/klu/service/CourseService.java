package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Course;
import com.klu.model.Enrollment;
import com.klu.repository.CourseRepository;
import com.klu.repository.EnrollmentRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repo;

    @Autowired
    private EnrollmentRepository enrollmentRepo; // 🔥 ADD THIS

    // 👨‍🏫 Create Course
    public Course createCourse(Course course) {
        return repo.save(course);
    }

    // 👨‍🎓 Get All Courses
    public List<Course> getAllCourses() {
        return repo.findAll();
    }

    // 🔥 NEW → Get My Courses (IMPORTANT)
    public List<Course> getMyCourseDetails(String email) {

        List<Enrollment> enrollments = enrollmentRepo.findByStudentEmail(email);

        return enrollments.stream()
                .map(e -> repo.findById(e.getCourseId()).orElse(null))
                .toList();
    }
}
package com.klu.service;

import java.util.List;
import java.util.Objects;

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
    public Course createCourse(Course course, String educatorEmail) {
        course.setId(null);
        course.setEducatorEmail(educatorEmail);
        return repo.save(course);
    }

    // 👨‍🏫 Update Course
    public Course updateCourse(Long courseId, Course updatedCourse, String educatorEmail) {
        Course existingCourse = getOwnedCourse(courseId, educatorEmail);

        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setPrice(updatedCourse.getPrice());
        existingCourse.setVideoUrl(updatedCourse.getVideoUrl());
        existingCourse.setFileUrl(updatedCourse.getFileUrl());

        return repo.save(existingCourse);
    }

    // 👨‍🎓 Get All Courses
    public List<Course> getAllCourses() {
        return repo.findAll();
    }

    public Course getCourseById(Long courseId) {
        return repo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public List<Course> getEducatorCourses(String educatorEmail) {
        return repo.findAll().stream()
                .filter(course -> Objects.equals(course.getEducatorEmail(), educatorEmail))
                .toList();
    }

    // 🔥 NEW → Get My Courses (IMPORTANT)
    public List<Course> getMyCourseDetails(String email) {

        List<Enrollment> enrollments = enrollmentRepo.findByStudentEmail(email);

        return enrollments.stream()
                .map(e -> repo.findById(e.getCourseId()).orElse(null))
                .filter(Objects::nonNull)
                .toList();
    }

    public Course updateCourseFile(Long courseId, String fileUrl, String educatorEmail) {
        Course course = getOwnedCourse(courseId, educatorEmail);
        course.setFileUrl(fileUrl);
        return repo.save(course);
    }

    private Course getOwnedCourse(Long courseId, String educatorEmail) {
        Course course = getCourseById(courseId);

        if (!Objects.equals(course.getEducatorEmail(), educatorEmail)) {
            throw new RuntimeException("You can only modify your own course");
        }

        return course;
    }
}

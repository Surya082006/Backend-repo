package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Enrollment;
import com.klu.repository.CourseRepository;
import com.klu.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository repo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CourseRepository courseRepo;

    // 👨‍🎓 Enroll student
    public Enrollment enroll(String email, Long courseId) {
        Enrollment e = new Enrollment();
        e.setUserEmail(email);
        e.setCourseId(courseId);
        e.setProgress(0);
        Enrollment saved = repo.save(e);

        courseRepo.findById(courseId).ifPresent(course -> {
            String subject = "Successful Registration for " + course.getTitle();
            String text = "Hello,\n\nYou have successfully registered for the course: " + course.getTitle() + ".\n\nHappy Learning!";
            emailService.sendEmail(email, subject, text);
        });

        return saved;
    }

    // ❌ Unenroll
    public void unenroll(String email, Long courseId) {
        repo.deleteByUserEmailAndCourseId(email, courseId);
    }

    // 📚 Get student courses
    public List<Enrollment> getByUser(String email) {
        return repo.findByUserEmail(email);
    }

    // 👨‍🏫 Get all students (simple)
    public List<Enrollment> getAll() {
        return repo.findAll();
    }
}
package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Enrollment;
import com.klu.repository.EnrollmentRepository;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository repo;

    // 👨‍🎓 Enroll student
    public Enrollment enroll(String email, Long courseId) {
        Enrollment e = new Enrollment();
        e.setUserEmail(email);
        e.setCourseId(courseId);
        e.setProgress(0);
        return repo.save(e);
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
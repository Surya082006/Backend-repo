package com.klu.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Course;
import com.klu.model.User;
import com.klu.model.Enrollment;
import com.klu.repository.CourseRepository;
import com.klu.repository.EnrollmentRepository;
import com.klu.repository.UserRepository;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository repo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private UserRepository userRepo;

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

    // 👨‍🏫 Get all students (simple raw)
    public List<Enrollment> getAll() {
        return repo.findAll();
    }

    // 🌟 Get comprehensive Platform Students
    public List<Map<String, Object>> getPlatformStudents() {
        List<User> students = userRepo.findAll().stream()
                .filter(u -> "student".equalsIgnoreCase(u.getRole()))
                .toList();

        List<Enrollment> enrollments = repo.findAll();
        List<Course> courses = courseRepo.findAll();

        List<Map<String, Object>> result = new ArrayList<>();

        for (User student : students) {
            boolean isEnrolled = false;
            for (Enrollment e : enrollments) {
                if (e.getUserEmail().equals(student.getEmail())) {
                    isEnrolled = true;
                    Course c = courses.stream()
                        .filter(course -> course.getId().equals(e.getCourseId()))
                        .findFirst().orElse(null);

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", student.getUsername());
                    map.put("email", student.getEmail());
                    map.put("course", c != null ? c.getTitle() : "Course Not Found");
                    map.put("code", c != null ? c.getId() : "-");
                    map.put("progress", e.getProgress());
                    result.add(map);
                }
            }
            if (!isEnrolled) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", student.getUsername());
                map.put("email", student.getEmail());
                map.put("course", "Not Enrolled");
                map.put("code", "-");
                map.put("progress", 0);
                result.add(map);
            }
        }
        return result;
    }
}
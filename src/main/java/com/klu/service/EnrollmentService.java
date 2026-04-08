package com.klu.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        String normalizedEmail = normalizeEmail(email);
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        repo.findByUserEmailIgnoreCaseAndCourseId(normalizedEmail, courseId).ifPresent(existing -> {
            throw new RuntimeException("Student is already enrolled in this course");
        });

        Enrollment e = new Enrollment();
        e.setUserEmail(normalizedEmail);
        e.setCourseId(courseId);
        e.setProgress(0);
        Enrollment saved = repo.save(e);

        String subject = "Successful Registration for " + course.getTitle();
        String text = "Hello,\n\nYou have successfully registered for the course: " + course.getTitle() + ".\n\nHappy Learning!";
        emailService.sendEmail(normalizedEmail, subject, text);

        return saved;
    }

    public Enrollment assignStudentToCourse(String educatorEmail, String studentEmail, Long courseId) {
        String normalizedEducatorEmail = normalizeEmail(educatorEmail);
        String normalizedStudentEmail = normalizeEmail(studentEmail);

        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!normalizedEducatorEmail.equalsIgnoreCase(course.getEducatorEmail())) {
            throw new RuntimeException("You can only assign your own courses");
        }

        User student = userRepo.findByEmail(normalizedStudentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!"STUDENT".equalsIgnoreCase(student.getRole())) {
            throw new RuntimeException("Selected user is not a student");
        }

        return enroll(normalizedStudentEmail, courseId);
    }

    // ❌ Unenroll
    @Transactional
    public void unenroll(String email, Long courseId) {
        String normalizedEmail = normalizeEmail(email);

        Enrollment enrollment = repo.findByUserEmailIgnoreCaseAndCourseId(normalizedEmail, courseId)
                .orElseThrow(() -> new RuntimeException("You are not enrolled in this course"));

        repo.delete(enrollment);
    }

    // 📚 Get student courses
    public List<Enrollment> getByUser(String email) {
        return repo.findByUserEmailIgnoreCase(email);
    }

    // 👨‍🏫 Get all students (simple raw)
    public List<Enrollment> getAll() {
        return repo.findAll();
    }

    // 🌟 Get comprehensive Platform Students
    public List<Map<String, Object>> getEducatorStudents(String educatorEmail) {
        List<Course> courses = courseRepo.findByEducatorEmailIgnoreCase(educatorEmail);
        Set<Long> ownedCourseIds = new HashSet<>(courses.stream().map(Course::getId).toList());
        Map<Long, Course> courseById = new HashMap<>();
        for (Course course : courses) {
            courseById.put(course.getId(), course);
        }

        List<User> students = userRepo.findAllByRoleIgnoreCaseOrderByUsernameAsc("STUDENT");

        List<Enrollment> enrollments = repo.findAll().stream()
                .filter(e -> ownedCourseIds.contains(e.getCourseId()))
                .toList();

        List<Map<String, Object>> result = new ArrayList<>();

        for (User student : students) {
            boolean isEnrolled = false;
            for (Enrollment e : enrollments) {
                if (e.getUserEmail().equals(student.getEmail())) {
                    isEnrolled = true;
                    Course c = courseById.get(e.getCourseId());

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
        result.sort(Comparator.comparing(entry -> String.valueOf(entry.get("name"))));
        return result;
    }

    public List<Map<String, String>> getAllStudents() {
        return userRepo.findAllByRoleIgnoreCaseOrderByUsernameAsc("STUDENT").stream()
                .map(user -> {
                    Map<String, String> student = new HashMap<>();
                    student.put("name", user.getUsername());
                    student.put("email", user.getEmail());
                    return student;
                })
                .toList();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}

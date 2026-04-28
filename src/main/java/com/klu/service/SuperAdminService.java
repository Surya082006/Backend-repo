package com.klu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.*;
import com.klu.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuperAdminService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private EmailService emailService;

    public List<User> getPendingEducators() {
        return userRepo.findAllByRoleIgnoreCaseOrderByUsernameAsc("EDUCATOR").stream()
                .filter(u -> !u.isApproved())
                .collect(Collectors.toList());
    }

    public List<User> getApprovedEducators() {
        return userRepo.findAllByRoleIgnoreCaseOrderByUsernameAsc("EDUCATOR").stream()
                .filter(User::isApproved)
                .collect(Collectors.toList());
    }

    public List<User> getAllStudents() {
        return userRepo.findAllByRoleIgnoreCaseOrderByUsernameAsc("STUDENT");
    }

    public void approveEducator(Long educatorId) {
        User user = userRepo.findById(educatorId)
                .orElseThrow(() -> new RuntimeException("Educator not found"));

        if (!"EDUCATOR".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("User is not an educator");
        }

        user.setApproved(true);
        userRepo.save(user);

        String subject = "Your CourseSphere Educator Account is Approved!";
        String text = "Hello " + user.getUsername() + ",\n\n"
                + "Great news! Your educator account on CourseSphere has been approved by the Super Admin.\n"
                + "You can now log in and start creating courses.\n\n"
                + "Welcome aboard!\nCourseSphere Team";
        
        emailService.sendEmail(user.getEmail(), subject, text);
    }
}

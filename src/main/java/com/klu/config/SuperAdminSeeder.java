package com.klu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.klu.model.User;
import com.klu.model.Student;
import com.klu.repository.UserRepository;

@Component
public class SuperAdminSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.findAllByRoleIgnoreCaseOrderByUsernameAsc("SUPERADMIN").isEmpty()) {
            Student superAdmin = new Student();
            superAdmin.setUsername("Admin");
            superAdmin.setEmail("admin@coursesphere.com");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setRole("SUPERADMIN");
            superAdmin.setPhone("1234567890");
            superAdmin.setApproved(true);
            superAdmin.setDepartment("Admin");
            superAdmin.setYearSemester("N/A");
            
            userRepo.save(superAdmin);
            System.out.println("✅ Default Super Admin created! Email: admin@coursesphere.com | Password: admin123");
        }
    }
}

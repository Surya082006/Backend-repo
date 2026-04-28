package com.klu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.User;
import com.klu.model.Student;
import com.klu.model.Educator;
import com.klu.dto.UserDTO;
import com.klu.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

   
    public User getUser(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

   
    public User updateUser(String email, UserDTO updatedDTO) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(updatedDTO.getUsername());
        user.setPhone(updatedDTO.getPhone());
        
        if (user instanceof Student student) {
            student.setDepartment(updatedDTO.getDepartment());
            student.setYearSemester(updatedDTO.getYearSemester());
        } else if (user instanceof Educator educator) {
            educator.setQualification(updatedDTO.getQualification());
            educator.setSpecialization(updatedDTO.getSpecialization());
        }

        return userRepo.save(user);
    }
}
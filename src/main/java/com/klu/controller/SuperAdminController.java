package com.klu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.klu.model.*;
import com.klu.service.SuperAdminService;

import java.util.List;

@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

    @Autowired
    private SuperAdminService service;

    @GetMapping("/educators/pending")
    public List<User> getPendingEducators() {
        return service.getPendingEducators();
    }

    @GetMapping("/educators/approved")
    public List<User> getApprovedEducators() {
        return service.getApprovedEducators();
    }

    @GetMapping("/students")
    public List<User> getAllStudents() {
        return service.getAllStudents();
    }

    @PutMapping("/educators/approve/{id}")
    public String approveEducator(@PathVariable Long id) {
        service.approveEducator(id);
        return "Educator approved successfully";
    }
}

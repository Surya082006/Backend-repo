package com.klu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.klu.model.Notification;
import com.klu.service.NotificationService;

@RestController
@RequestMapping("/api")
public class NotificationController {

    @Autowired
    private NotificationService service;

    // 🔔 Get notifications for logged-in student
    @GetMapping("/student/notifications")
    public List<Notification> getMyNotifications(Authentication auth) {
        return service.getUserNotifications(auth.getName());
    }

    // (Optional) create notification manually
    @PostMapping("/notification")
    public Notification create(@RequestBody Notification notification) {
        return service.create(notification);
    }
}
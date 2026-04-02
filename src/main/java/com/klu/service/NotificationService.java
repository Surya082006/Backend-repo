package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Notification;
import com.klu.repository.NotificationRepository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    // 🔔 Save notification
    public Notification create(Notification notification) {
        return repo.save(notification);
    }

    // 📩 Get notifications for user
    public List<Notification> getUserNotifications(String email) {
        return repo.findByUserEmail(email);
    }
}
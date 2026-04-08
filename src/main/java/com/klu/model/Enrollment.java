package com.klu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userEmail; 
    private Long courseId;

    private Integer progress = 0;

    public Enrollment() {
    }

    public Enrollment(Long id, String userEmail, Long courseId, Integer progress) {
        this.id = id;
        this.userEmail = userEmail;
        this.courseId = courseId;
        this.progress = progress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}

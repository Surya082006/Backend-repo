package com.klu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long assignmentId;
    private String studentEmail;
    private String fileUrl;
    private String description;
    private Integer grade; 

    public Submission() {
    }

    public Submission(Long id, Long assignmentId, String studentEmail, String fileUrl, String description, Integer grade) {
        this.id = id;
        this.assignmentId = assignmentId;
        this.studentEmail = studentEmail;
        this.fileUrl = fileUrl;
        this.description = description;
        this.grade = grade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}

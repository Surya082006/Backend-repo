package com.klu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String question;
    private Long courseId;
    private String educatorEmail;

    @Transient
    private Boolean submitted;

    @Transient
    private Integer grade;

    public Assignment() {
    }

    public Assignment(Long id, String title, String question, Long courseId, String educatorEmail) {
        this.id = id;
        this.title = title;
        this.question = question;
        this.courseId = courseId;
        this.educatorEmail = educatorEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getEducatorEmail() {
        return educatorEmail;
    }

    public void setEducatorEmail(String educatorEmail) {
        this.educatorEmail = educatorEmail;
    }

    public Boolean getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Boolean submitted) {
        this.submitted = submitted;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
}

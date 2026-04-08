package com.klu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answer;
    private String educatorEmail;
    private Long doubtId;

    public Reply() {
    }

    public Reply(Long id, String answer, String educatorEmail, Long doubtId) {
        this.id = id;
        this.answer = answer;
        this.educatorEmail = educatorEmail;
        this.doubtId = doubtId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getEducatorEmail() {
        return educatorEmail;
    }

    public void setEducatorEmail(String educatorEmail) {
        this.educatorEmail = educatorEmail;
    }

    public Long getDoubtId() {
        return doubtId;
    }

    public void setDoubtId(Long doubtId) {
        this.doubtId = doubtId;
    }
}

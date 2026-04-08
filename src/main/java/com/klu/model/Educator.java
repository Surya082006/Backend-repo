package com.klu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "educators")
@Data
@EqualsAndHashCode(callSuper = true)
public class Educator extends User {
    private String qualification;
    private String specialization;

    public Educator() {
    }

    public Educator(Long id, String username, String email, String password, String role, String phone,
                    String qualification, String specialization) {
        super(id, username, email, password, role, phone);
        this.qualification = qualification;
        this.specialization = specialization;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}

package com.klu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "students")
@Data
@EqualsAndHashCode(callSuper = true)
public class Student extends User {
    private String department;
    private String yearSemester;

    public Student() {
    }

    public Student(Long id, String username, String email, String password, String role, String phone,
                   String department, String yearSemester) {
        super(id, username, email, password, role, phone);
        this.department = department;
        this.yearSemester = yearSemester;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getYearSemester() {
        return yearSemester;
    }

    public void setYearSemester(String yearSemester) {
        this.yearSemester = yearSemester;
    }
}

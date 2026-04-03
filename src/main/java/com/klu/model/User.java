package com.klu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // 🔥 prevents table conflicts
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role; // STUDENT / EDUCATOR

 

    private String phone;

    private String qualification;

    private String department;
}
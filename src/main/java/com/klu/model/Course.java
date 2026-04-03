package com.klu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private Double price;

    private String educatorEmail;
    private String videoUrl;
    private String fileUrl;
    private String imageUrl;
    private String category;
    private String duration;
}
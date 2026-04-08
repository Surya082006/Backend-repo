package com.klu.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
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

    @Transient
    private Integer progress;

    public Course() {
    }

    public Course(Long id, String title, String description, Double price, String educatorEmail, String videoUrl,
                  String fileUrl, String imageUrl, String category, String duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.educatorEmail = educatorEmail;
        this.videoUrl = videoUrl;
        this.fileUrl = fileUrl;
        this.imageUrl = imageUrl;
        this.category = category;
        this.duration = duration;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getEducatorEmail() {
        return educatorEmail;
    }

    public void setEducatorEmail(String educatorEmail) {
        this.educatorEmail = educatorEmail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
}

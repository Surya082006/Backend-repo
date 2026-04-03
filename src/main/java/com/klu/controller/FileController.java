package com.klu.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.klu.model.Course;
import com.klu.service.CourseService;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private static final Path UPLOAD_DIR = Paths.get("uploads");

    @Autowired
    private CourseService courseService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {
        return storeFile(file);
    }

    @PostMapping("/upload/course/{courseId}")
    public Course uploadCourseFile(@PathVariable Long courseId,
                                   @RequestParam("file") MultipartFile file,
                                   Authentication auth) throws Exception {
        String fileUrl = storeFile(file);
        return courseService.updateCourseFile(courseId, fileUrl, auth.getName());
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<Resource> download(@PathVariable String name) throws Exception {
        Path path = UPLOAD_DIR.resolve(name).normalize();
        Resource resource = new UrlResource(path.toUri());

        if (!path.startsWith(UPLOAD_DIR) || !Files.exists(path) || !resource.exists()) {
            throw new RuntimeException("File not found");
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(resource);
    }

    private String storeFile(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        File dir = UPLOAD_DIR.toFile();
        if (!dir.exists()) {
            Files.createDirectories(UPLOAD_DIR);
        }

        String originalName = Path.of(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename())
                .getFileName()
                .toString();
        String storedName = UUID.randomUUID() + "-" + originalName;
        Path targetPath = UPLOAD_DIR.resolve(storedName).normalize();

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return "/api/file/download/" + storedName;
    }
}

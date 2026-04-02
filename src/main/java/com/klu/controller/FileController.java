package com.klu.controller;

import org.springframework.core.io.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.*;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws Exception {

        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        String path = UPLOAD_DIR + file.getOriginalFilename();
        file.transferTo(new File(path));

        return "Uploaded: " + path;
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<Resource> download(@PathVariable String name) throws Exception {

        Path path = Paths.get(UPLOAD_DIR + name);
        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(resource);
    }
}
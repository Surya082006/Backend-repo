package com.klu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.klu.model.Doubt;
import com.klu.model.Reply;
import com.klu.service.DoubtService;

@RestController
@RequestMapping("/api")
public class DoubtController {

    @Autowired
    private DoubtService service;

    // 👨‍🎓 Ask doubt
    @PostMapping("/student/doubt")
    public Doubt ask(@RequestBody Doubt doubt, Authentication auth) {
        doubt.setStudentEmail(auth.getName());
        return service.askDoubt(doubt);
    }

    // 👨‍🏫 View all doubts
    @GetMapping("/educator/doubts")
    public List<Doubt> getAll() {
        return service.getAllDoubts();
    }

    // 👨‍🏫 Reply to doubt
    @PostMapping("/educator/reply")
    public Reply reply(@RequestBody Reply reply, Authentication auth) {
        reply.setEducatorEmail(auth.getName());
        return service.reply(reply);
    }
}
package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Doubt;
import com.klu.model.Reply;
import com.klu.model.Notification;
import com.klu.repository.DoubtRepository;
import com.klu.repository.ReplyRepository;

@Service
public class DoubtService {

    @Autowired
    private DoubtRepository doubtRepo;

    @Autowired
    private ReplyRepository replyRepo;

    @Autowired
    private NotificationService notificationService;

    // 👨‍🎓 Student asks doubt
    public Doubt askDoubt(Doubt doubt) {
        return doubtRepo.save(doubt);
    }

    // 👨‍🏫 Educator sees doubts
    public List<Doubt> getAllDoubts() {
        return doubtRepo.findAll();
    }

    // 👨‍🏫 Educator replies
    public Reply reply(Reply reply) {

        // Save reply
        Reply savedReply = replyRepo.save(reply);

        // 🔥 Fetch doubt using doubtId
        Doubt doubt = doubtRepo.findById(reply.getDoubtId()).orElse(null);

        if (doubt != null) {
            // 🔔 Create notification
            Notification n = new Notification();
            n.setMessage("Your doubt has been answered");

            // ✅ Correct student email
            n.setUserEmail(doubt.getStudentEmail());

            notificationService.create(n);
        }

        return savedReply;
    }
}
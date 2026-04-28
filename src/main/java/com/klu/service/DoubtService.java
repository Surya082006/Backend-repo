package com.klu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.klu.model.Course;
import com.klu.model.Doubt;
import com.klu.model.Reply;
import com.klu.model.Notification;
import com.klu.model.Enrollment;
import com.klu.repository.CourseRepository;
import com.klu.repository.DoubtRepository;
import com.klu.repository.EnrollmentRepository;
import com.klu.repository.ReplyRepository;
import com.klu.exception.CourseCompletedException;

@Service
public class DoubtService {

    @Autowired
    private DoubtRepository doubtRepo;

    @Autowired
    private ReplyRepository replyRepo;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private CourseRepository courseRepo;

    
    public Doubt askDoubt(Doubt doubt) {
        Enrollment enrollment = enrollmentRepo.findByUserEmailIgnoreCaseAndCourseId(
                doubt.getStudentEmail(),
                doubt.getCourseId()
        ).orElseThrow(() -> new RuntimeException("Enroll in the course before raising a doubt"));
        
        if (enrollment != null && enrollment.getProgress() >= 100) {
            throw new CourseCompletedException("Your course is completed if you have a doubt please contact educator from a mail for response");
        }

        return doubtRepo.save(doubt);
    }

    
    public List<Doubt> getAllDoubts(String educatorEmail) {
        List<Long> courseIds = courseRepo.findByEducatorEmailIgnoreCase(educatorEmail).stream()
                .map(Course::getId)
                .toList();

        if (courseIds.isEmpty()) {
            return List.of();
        }

        return doubtRepo.findByCourseIdIn(courseIds);
    }

  
    public Reply reply(Reply reply, String educatorEmail) {
        Doubt doubt = doubtRepo.findById(reply.getDoubtId())
                .orElseThrow(() -> new RuntimeException("Doubt not found"));

        Course course = courseRepo.findById(doubt.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getEducatorEmail().equalsIgnoreCase(educatorEmail)) {
            throw new RuntimeException("You can only reply to doubts for your own courses");
        }

        reply.setEducatorEmail(educatorEmail);

        // Save reply
        Reply savedReply = replyRepo.save(reply);

        Notification n = new Notification();
        n.setMessage("Your doubt has been answered");
        n.setUserEmail(doubt.getStudentEmail());
        notificationService.create(n);

        return savedReply;
    }
}

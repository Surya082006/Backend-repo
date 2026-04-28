package com.klu.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.klu.model.Assignment;
import com.klu.model.Course;
import com.klu.model.Doubt;
import com.klu.model.Enrollment;
import com.klu.repository.AssignmentRepository;
import com.klu.repository.CourseRepository;
import com.klu.repository.DoubtRepository;
import com.klu.repository.EnrollmentRepository;
import com.klu.repository.ReplyRepository;
import com.klu.repository.SubmissionRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository repo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private AssignmentRepository assignmentRepo;

    @Autowired
    private SubmissionRepository submissionRepo;

    @Autowired
    private DoubtRepository doubtRepo;

    @Autowired
    private ReplyRepository replyRepo;

    
    public Course createCourse(Course course, String educatorEmail) {
        course.setId(null);
        course.setEducatorEmail(educatorEmail);
        return repo.save(course);
    }

    // 👨‍🏫 Update Course
    public Course updateCourse(Long courseId, Course updatedCourse, String educatorEmail) {
        Course existingCourse = getOwnedCourse(courseId, educatorEmail);

        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setPrice(updatedCourse.getPrice());
        existingCourse.setVideoUrl(updatedCourse.getVideoUrl());
        existingCourse.setFileUrl(updatedCourse.getFileUrl());

        return repo.save(existingCourse);
    }

    // 👨‍🎓 Get All Courses
    public List<Course> getAllCourses() {
        return repo.findAll();
    }

    // 👨‍🎓 Get Course By ID
    public Course getCourseById(Long courseId) {
        return repo.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course getEducatorCourse(Long courseId, String educatorEmail) {
        return getOwnedCourse(courseId, educatorEmail);
    }

    // 👨‍🏫 Get Educator Courses
    public List<Course> getEducatorCourses(String educatorEmail) {
        return repo.findByEducatorEmailIgnoreCase(educatorEmail);
    }

    // 🔥 FIXED → Get My Courses (Student)
    public List<Course> getMyCourseDetails(String email) {

        List<Enrollment> enrollments = enrollmentRepo.findByUserEmailIgnoreCase(email);
        Map<Long, Course> coursesById = new LinkedHashMap<>();

        for (Enrollment enrollment : enrollments) {
            Course course = repo.findById(enrollment.getCourseId()).orElse(null);
            if (course == null) {
                continue;
            }

            Course existing = coursesById.get(course.getId());
            if (existing == null || safeProgress(enrollment) > safeProgress(existing.getProgress())) {
                course.setProgress(safeProgress(enrollment));
                coursesById.put(course.getId(), course);
            }
        }

        return List.copyOf(coursesById.values());
    }

    // 📁 Update Course File
    public Course updateCourseFile(Long courseId, String fileUrl, String educatorEmail) {
        Course course = getOwnedCourse(courseId, educatorEmail);
        course.setFileUrl(fileUrl);
        return repo.save(course);
    }

    @Transactional
    public void deleteCourse(Long courseId, String educatorEmail) {
        Course course = getOwnedCourse(courseId, educatorEmail);

        List<Long> assignmentIds = assignmentRepo.findByCourseId(courseId).stream()
                .map(Assignment::getId)
                .toList();
        if (!assignmentIds.isEmpty()) {
            submissionRepo.deleteByAssignmentIdIn(assignmentIds);
        }
        assignmentRepo.deleteByCourseId(courseId);

        List<Long> doubtIds = doubtRepo.findByCourseIdIn(List.of(courseId)).stream()
                .map(Doubt::getId)
                .toList();
        if (!doubtIds.isEmpty()) {
            replyRepo.deleteByDoubtIdIn(doubtIds);
        }
        doubtRepo.deleteByCourseId(courseId);

        enrollmentRepo.deleteByCourseId(courseId);
        repo.delete(course);
    }

    // 🔒 Check ownership
    private Course getOwnedCourse(Long courseId, String educatorEmail) {
        Course course = getCourseById(courseId);

        if (!course.getEducatorEmail().equalsIgnoreCase(educatorEmail)) {
            throw new RuntimeException("You can only modify your own course");
        }

        return course;
    }

    private int safeProgress(Enrollment enrollment) {
        return safeProgress(enrollment == null ? null : enrollment.getProgress());
    }

    private int safeProgress(Integer progress) {
        return progress == null ? 0 : progress;
    }
}

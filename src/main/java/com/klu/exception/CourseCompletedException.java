package com.klu.exception;

public class CourseCompletedException extends RuntimeException {
    public CourseCompletedException(String message) {
        super(message);
    }
}

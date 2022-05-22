package com.assignment.visma_test_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AttendeeNotInMeetingException extends ResponseStatusException {
    public AttendeeNotInMeetingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
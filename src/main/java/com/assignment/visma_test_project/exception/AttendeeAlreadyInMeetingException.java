package com.assignment.visma_test_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AttendeeAlreadyInMeetingException extends ResponseStatusException {
    public AttendeeAlreadyInMeetingException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
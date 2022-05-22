package com.assignment.visma_test_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MeetingNotFoundException extends ResponseStatusException {
    public MeetingNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}
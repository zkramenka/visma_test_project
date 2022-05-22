package com.assignment.visma_test_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RequesterIsNotResponsiblePersonException extends ResponseStatusException {
    public RequesterIsNotResponsiblePersonException(String message){
        super(HttpStatus.UNAUTHORIZED, message);
    }
}
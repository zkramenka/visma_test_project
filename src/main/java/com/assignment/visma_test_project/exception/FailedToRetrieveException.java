package com.assignment.visma_test_project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FailedToRetrieveException extends ResponseStatusException {
    public FailedToRetrieveException(String message){
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}

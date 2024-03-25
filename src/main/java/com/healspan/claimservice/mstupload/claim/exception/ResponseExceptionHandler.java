package com.healspan.claimservice.mstupload.claim.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.healspan.claimservice.mstupload.claim.constants.Constant.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<ApplicationResponse> handleHttpMediaTypeNotSupported(RuntimeException ex, WebRequest request) {
        ApplicationResponse applicationResponse = new ApplicationResponse();
        applicationResponse.setStatus(ResponseStatus.FAILED);
        applicationResponse.setDateTime(LocalDateTime.now());
        applicationResponse.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(applicationResponse, HttpStatus.BAD_REQUEST);
    }
}

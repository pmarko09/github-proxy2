package com.github_proxy2.github_proxy2.exception;

import com.github_proxy2.github_proxy2.model.dto.ErrorMessageDto;
import feign.FeignException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RepositoryAlreadyExistsException.class)
    protected ResponseEntity<ErrorMessageDto> handleRepoAlreadyExists(RepositoryAlreadyExistsException ex) {
        ErrorMessageDto body = new ErrorMessageDto(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RepositoryNotFoundException.class)
    protected ResponseEntity<ErrorMessageDto> handleRepoNotFound(RepositoryNotFoundException ex) {
        ErrorMessageDto body = new ErrorMessageDto(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.BadRequest.class)
    protected ResponseEntity<ErrorMessageDto> handleFeignBadRequest(FeignException ex) {
        ErrorMessageDto body = new ErrorMessageDto(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.NotFound.class)
    protected ResponseEntity<ErrorMessageDto> handleFeignNotFound(FeignException ex) {
        ErrorMessageDto body = new ErrorMessageDto(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.InternalServerError.class)
    protected ResponseEntity<ErrorMessageDto> handleFeignServerError(FeignException ex) {
        ErrorMessageDto body = new ErrorMessageDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorMessageDto> handleGeneralException(Exception ex) {
        ErrorMessageDto body = new ErrorMessageDto(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(body, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

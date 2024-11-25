package com.github_proxy2.github_proxy2.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessageDto {

    private HttpStatus status;
    private String error;
    private String message;
    private LocalDateTime timestamp;
}

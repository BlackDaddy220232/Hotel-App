package com.tasks.hotelapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
@AllArgsConstructor
public class ResponseError {
    private Integer httpStatusCode;
    private HttpStatus status;
    private String message;
}

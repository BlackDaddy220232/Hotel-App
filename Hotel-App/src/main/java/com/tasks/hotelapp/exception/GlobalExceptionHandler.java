package com.tasks.hotelapp.exception;

import com.tasks.hotelapp.model.dto.ResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HotelAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseError handleHotelAlreadyExistsException(HotelAlreadyExistsException exception){
        return new ResponseError(409,HttpStatus.CONFLICT,exception.getMessage());
    }

    @ExceptionHandler(HotelNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleHotelNotFoundException(HotelNotFoundException exception){
        return new ResponseError(404,HttpStatus.NOT_FOUND,exception.getMessage());
    }

    @ExceptionHandler(NotValidDataException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleHotelNotFoundException(NotValidDataException exception){
        return new ResponseError(400,HttpStatus.BAD_REQUEST,exception.getMessage());
    }


}

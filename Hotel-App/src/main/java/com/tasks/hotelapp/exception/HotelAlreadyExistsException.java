package com.tasks.hotelapp.exception;


public class HotelAlreadyExistsException extends RuntimeException {
    public HotelAlreadyExistsException(String message){
        super(message);
    }
}

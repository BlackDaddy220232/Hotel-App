package com.tasks.hotelapp.exception;

import com.tasks.hotelapp.model.entity.Hotel;

public class HotelAlreadyExistsException extends RuntimeException {
    public HotelAlreadyExistsException(String message){
        super(message);
    }
}

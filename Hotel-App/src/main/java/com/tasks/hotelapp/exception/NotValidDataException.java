package com.tasks.hotelapp.exception;

public class NotValidDataException extends RuntimeException{
    public NotValidDataException(String message){
        super(message);
    }
}

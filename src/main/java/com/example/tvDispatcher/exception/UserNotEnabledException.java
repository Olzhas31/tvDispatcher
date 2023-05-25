package com.example.tvDispatcher.exception;

public class UserNotEnabledException extends RuntimeException{

    public UserNotEnabledException(String message){
        super(message);
    }
}

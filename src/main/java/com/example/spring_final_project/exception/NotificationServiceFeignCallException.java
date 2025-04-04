package com.example.spring_final_project.exception;

public class NotificationServiceFeignCallException extends RuntimeException{

    public NotificationServiceFeignCallException(){

    }

    public NotificationServiceFeignCallException(String message) {
        super(message);
    }
}

package com.vvdn.ems_backend.exception;

public class LeaveAlreadyAllocatedException extends RuntimeException{

    public LeaveAlreadyAllocatedException(String message) {
        super(message);
    }
}

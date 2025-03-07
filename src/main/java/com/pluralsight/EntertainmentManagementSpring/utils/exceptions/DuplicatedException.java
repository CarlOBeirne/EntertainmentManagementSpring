package com.pluralsight.EntertainmentManagementSpring.utils.exceptions;

public class DuplicatedException extends RuntimeException {
    public DuplicatedException(String message) {
        super(message);
    }
}

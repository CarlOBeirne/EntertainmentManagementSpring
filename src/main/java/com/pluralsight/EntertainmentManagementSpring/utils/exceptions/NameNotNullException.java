package com.pluralsight.EntertainmentManagementSpring.utils.exceptions;

public class NameNotNullException extends RuntimeException {
    public NameNotNullException(String message) {
        super(message);
    }
}

package com.pluralsight.EntertainmentManagementSpring.utils.exceptions;

public class TitleNotNullException extends RuntimeException {
    public TitleNotNullException(String message) {
        super(message);
    }
}

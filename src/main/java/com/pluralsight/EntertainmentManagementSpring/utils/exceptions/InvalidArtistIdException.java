package com.pluralsight.EntertainmentManagementSpring.utils.exceptions;

public class InvalidArtistIdException extends RuntimeException {
    public InvalidArtistIdException(String message) {
        super(message);
    }
}

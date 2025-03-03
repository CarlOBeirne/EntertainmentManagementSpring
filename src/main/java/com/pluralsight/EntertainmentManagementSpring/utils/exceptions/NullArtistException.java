package com.pluralsight.EntertainmentManagementSpring.utils.exceptions;

public class NullArtistException extends RuntimeException {
    public NullArtistException(String message) {
        super(message);
    }
}

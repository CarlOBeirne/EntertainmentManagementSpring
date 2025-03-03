package com.pluralsight.EntertainmentManagementSpring.utils.exceptions;

public class NoArtistFoundException extends RuntimeException {
    public NoArtistFoundException(String message) {
        super(message);
    }
}

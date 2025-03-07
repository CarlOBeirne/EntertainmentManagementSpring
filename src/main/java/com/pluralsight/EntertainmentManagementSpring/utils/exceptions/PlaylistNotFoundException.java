package com.pluralsight.EntertainmentManagementSpring.utils.exceptions;

public class PlaylistNotFoundException extends RuntimeException {
    public PlaylistNotFoundException(String message) {
        super(message);
    }
}

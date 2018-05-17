package se.group.backendgruppuppgift.tasker.service.exception;

public final class InvalidUserException extends RuntimeException {

    public InvalidUserException(String message) {
        super(message);
    }
}

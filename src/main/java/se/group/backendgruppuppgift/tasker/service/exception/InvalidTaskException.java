package se.group.backendgruppuppgift.tasker.service.exception;

public final class InvalidTaskException extends RuntimeException {

    public InvalidTaskException(String message) {
        super(message);
    }
}

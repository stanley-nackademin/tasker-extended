package se.group.backendgruppuppgift.tasker.service;

public class InvalidTeamException extends RuntimeException{

    public InvalidTeamException(String message) {
        super(message);
    }
}

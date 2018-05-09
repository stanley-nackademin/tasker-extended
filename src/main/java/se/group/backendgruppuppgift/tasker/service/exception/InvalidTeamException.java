package se.group.backendgruppuppgift.tasker.service.exception;

public class InvalidTeamException extends RuntimeException{

    public InvalidTeamException(String message){
        super(message);
    }
}

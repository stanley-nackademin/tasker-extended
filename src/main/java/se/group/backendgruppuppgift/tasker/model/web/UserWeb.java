package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.Team;

public final class UserWeb {

    private Long userNumber;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Team team;

    protected UserWeb(){}

    public UserWeb(Long userNumber, String username, String firstName, String lastName, Boolean isActive, Team team) {
        this.userNumber = userNumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
        this.team = team;
    }

    public Long getUserNumber() {
        return userNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return String.format("userNumber=%s, userName=%s, firstName=%s, lastName=%s, isActive=%s, team=%s", userNumber,username,firstName,lastName,isActive, team);
    }
}

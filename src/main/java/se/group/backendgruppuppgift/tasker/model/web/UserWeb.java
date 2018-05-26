package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.Team;

public final class UserWeb {

    private final Long userNumber;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final Boolean isActive;
    private final Team team;

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

    public Boolean getIsActive() {
        return isActive;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "UserWeb{" +
                "userNumber=" + userNumber +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", team=" + team +
                '}';
    }

}

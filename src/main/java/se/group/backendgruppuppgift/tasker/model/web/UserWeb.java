package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.*;

import java.util.Optional;

public final class UserWeb {

    private Long userNumber;
    private String username;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Team team;

    protected UserWeb() {
    }

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

    public Optional<User> getOptionalFromUser(UserWeb userWeb){
        User user = new User(userWeb.getUserNumber(), userWeb.getUsername()
                , userWeb.getFirstName(), userWeb.getLastName(), userWeb.getTeam());
        return Optional.ofNullable(user);
    }
}

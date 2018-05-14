package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.Team;

import java.util.Optional;

public final class UserWeb {

    private Long userNumber;
    private String userName;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private Team team;

    protected UserWeb() {
    }

    public UserWeb(Long userNumber, String username, String firstName, String lastName, Team team) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = true;
        this.team = team;
    }

    public Long getUserNumber() {
        return userNumber;
    }

    public String getUserName() {
        return userName;
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


    public static Optional<UserWeb> getOptionalFromUser(User user){
        UserWeb userWeb = new UserWeb(user.getUserNumber(), user.getUserName(), user.getFirstName(), user.getLastName());
        return Optional.ofNullable(userWeb);
    }
}

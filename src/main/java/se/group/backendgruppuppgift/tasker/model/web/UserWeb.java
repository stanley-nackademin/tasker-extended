package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.*;

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

    public UserWeb(Long userNumber, String userName, String firstName, String lastName, Boolean isActive, Team team) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
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
                ", username='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", team=" + team +
                '}';
    }

    public Optional<User> getOptionalFromUser(UserWeb webUser){
        User user = new User(webUser.getUserNumber(), webUser.getUserName()
                , webUser.getFirstName(), webUser.getLastName()
                ,  webUser.getTeam());
        return Optional.ofNullable(user);
    }

//    public Optional<UserWeb> convertToWeb(User user) {
//        return Optional.ofNullable(Optional<UserWeb>(user.getUserNumber(), user.getUsername(), user.getFirstName(), user.getLastName(),user.getIsActive(), user.getTeam());)
//
//    }
}
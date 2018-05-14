package se.group.backendgruppuppgift.tasker.model.web;


import se.group.backendgruppuppgift.tasker.model.User;

import java.util.Optional;

public final class UserWeb {


    private Long userNumber;
    private String userName;
    private String firstName;
    private String lastName;
    private boolean active;

    protected UserWeb() {
    }

    public UserWeb(Long userNumber, String userName, String firstName, String lastName) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = true;
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

    public boolean isActive() {
        return active;
    }

    public static Optional<UserWeb> getOptionalFromUser(User user){
        UserWeb userWeb = new UserWeb(user.getUserNumber(), user.getUserName(), user.getFirstName(), user.getLastName());
        return Optional.ofNullable(userWeb);
    }
}

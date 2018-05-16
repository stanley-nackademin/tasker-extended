package se.group.backendgruppuppgift.tasker.resource.converter;

import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;

import java.util.Optional;

public class UserConverter{

    public static Optional<User> getOptionalUser(UserWeb userWeb){
        User user = new User(userWeb.getUserNumber(), userWeb.getUsername()
                , userWeb.getFirstName(), userWeb.getLastName(), userWeb.getTeam());
        return Optional.ofNullable(user);
    }

    public static Optional<UserWeb> getOptionalUserWeb(User user){
        UserWeb userWeb = new UserWeb(user.getUserNumber(), user.getUsername()
                , user.getFirstName(), user.getLastName(), user.getIsActive(), user.getTeam());
        return Optional.ofNullable(userWeb);
    }
}
package se.group.backendgruppuppgift.tasker.resource.converter;

import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;

import java.util.Optional;

public class UserConverter implements ConverterInterface<UserWeb, Optional<?>, User>{

    @Override
    public Optional<User> fromWebToEntityData(UserWeb userWeb) {
        User user = new User(userWeb.getUserNumber(), userWeb.getUsername()
                , userWeb.getFirstName(), userWeb.getLastName(), userWeb.getTeam());
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<UserWeb> fromEntityToWebData(User user) {
        UserWeb userWeb = new UserWeb(user.getUserNumber(), user.getUsername()
                , user.getFirstName(), user.getLastName(), user.getIsActive(), user.getTeam());
        return Optional.ofNullable(userWeb);
    }
}

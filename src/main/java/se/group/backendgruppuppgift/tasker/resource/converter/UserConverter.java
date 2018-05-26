package se.group.backendgruppuppgift.tasker.resource.converter;

import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;

public final class UserConverter implements ConverterInterface<UserWeb, Object, User> {

    @Override
    public User fromWebToEntityData(UserWeb userWeb) {
        return new User(userWeb.getUserNumber(), userWeb.getUsername(),
                userWeb.getFirstName(), userWeb.getLastName(), userWeb.getTeam());
    }

    @Override
    public UserWeb fromEntityToWebData(User user) {
        return new UserWeb(user.getUserNumber(), user.getUsername(),
                user.getFirstName(), user.getLastName(), user.getIsActive(), user.getTeam());
    }
}

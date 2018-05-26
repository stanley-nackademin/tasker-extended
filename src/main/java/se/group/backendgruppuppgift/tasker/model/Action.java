package se.group.backendgruppuppgift.tasker.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class Action {

    private final String action;

    @JsonCreator
    public Action(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }
}

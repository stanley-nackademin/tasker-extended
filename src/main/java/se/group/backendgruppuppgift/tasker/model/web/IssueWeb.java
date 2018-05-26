package se.group.backendgruppuppgift.tasker.model.web;

import com.fasterxml.jackson.annotation.JsonCreator;

public final class IssueWeb {

    private final String description;

    @JsonCreator
    public IssueWeb(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format(
                "IssueWeb[description='%s']",
                description
        );
    }
}

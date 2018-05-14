package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.TaskStatus;
import se.group.backendgruppuppgift.tasker.model.User;

public final class TaskWeb {

    private Long id;
    private String description;
    private TaskStatus status;

    protected TaskWeb() {}

    public TaskWeb(Long id, String description, TaskStatus status) {
        this.id = id;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format(
                "TaskWeb[id=%d, description='%s', status='%s']",
                id, description, status
        );
    }
}

package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.TaskStatus;

public final class TaskWeb {

    private Long id;
    private String name;
    private TaskStatus status;

    protected TaskWeb() {}

    public TaskWeb(Long id, String name, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format(
                "Task[name='%s', status='%s']",
                name, status
        );
    }
}

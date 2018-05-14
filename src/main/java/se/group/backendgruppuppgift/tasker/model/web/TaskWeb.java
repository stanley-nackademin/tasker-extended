package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.TaskStatus;

public final class TaskWeb {


    private Long taskNumber;
    private String name;
    private TaskStatus status;

    protected TaskWeb() {}

    public TaskWeb(String name, TaskStatus status) {
        this.name = name;
        this.status = status;
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
                "User[name='%s', status='%s'",
                name, status
        );
    }


}

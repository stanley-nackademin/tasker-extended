package se.group.backendgruppuppgift.tasker.model.web;

import se.group.backendgruppuppgift.tasker.model.TaskStatus;

public final class TaskWeb {

    private final Long id;
    private final String description;
    private final TaskStatus status;
    private final IssueWeb issue;

    public TaskWeb(Long id, String description, TaskStatus status, IssueWeb issue) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.issue = issue;
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

    public IssueWeb getIssue() {
        return issue;
    }

    @Override
    public String toString() {
        return String.format(
                "TaskWeb[id=%d, description='%s', status='%s', issue=%s]",
                id, description, status, issue
        );
    }
}

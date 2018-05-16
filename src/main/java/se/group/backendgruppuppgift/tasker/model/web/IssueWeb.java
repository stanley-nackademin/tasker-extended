package se.group.backendgruppuppgift.tasker.model.web;

public final class IssueWeb {

    private String description;
    private boolean isDone;

    protected IssueWeb() {}

    public IssueWeb(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return String.format(
                "IssueWeb[description='%s', isDone='%s']",
                description, isDone
        );
    }
}

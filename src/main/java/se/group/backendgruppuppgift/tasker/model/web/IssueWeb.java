package se.group.backendgruppuppgift.tasker.model.web;

public final class IssueWeb {

    private String description;

    protected IssueWeb() {
    }

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

package se.group.backendgruppuppgift.tasker.model;

public enum TaskStatus {
    STARTED("started"),
    UNSTARTED("unstarted"),
    DONE("done");

    private String name;

    TaskStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

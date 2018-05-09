package se.group.backendgruppuppgift.tasker.model.web;

public final class TeamWeb {

    private String name;
    private Boolean isActive;

    protected TeamWeb() {
    }

    public TeamWeb(String name, Boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "TeamWeb{" +
                "name='" + name + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}

package se.group.backendgruppuppgift.tasker.model.web;

public final class TeamWeb {

    private Long id;
    private String name;
    private Boolean isActive;

    protected TeamWeb() {
    }

    public TeamWeb(Long id, String name, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return String.format(
                "TeamWeb[id=%d, name='%s', isActive=%s]",
                id, name, isActive
        );
    }
}

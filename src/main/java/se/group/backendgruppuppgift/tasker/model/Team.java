package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public final class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isActive;

    protected Team() {}

    public Team(String name, boolean isActive) {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return String.format(
                "Team[id=%d, name='%s', isActive=%s]",
                id, name, isActive
        );
    }
}

package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public final class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    protected Issue() {
    }

    public Issue(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format(
                "Issue[id=%d, description='%s']",
                id, description
        );
    }
}

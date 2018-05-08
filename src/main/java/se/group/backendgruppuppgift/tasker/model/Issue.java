package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public final class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @OneToOne
    @JoinColumn
    private Task task;

    @Column(nullable = false)
    private boolean isDone;

    protected Issue() {}

    public Issue(String description, Task task) {
        this.description = description;
        this.task = task;
        this.isDone = false;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Task getTask() {
        return task;
    }

    public boolean isDone() {
        return isDone;
    }
}

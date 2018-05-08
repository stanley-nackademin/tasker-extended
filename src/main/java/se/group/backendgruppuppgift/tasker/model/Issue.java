package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public class Issue {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private boolean done;
    @Column(nullable = false)
    private String description;

    @OneToOne
    private Task task;

    protected Issue() {
    }

    public Issue(String description, Task task) {
        this.description = description;
        this.task = task;
        this.done = false;
    }

    public Long getId() {
        return id;
    }

    public boolean isDone() {
        return done;
    }

    public String getDescription() {
        return description;
    }

    public Task getTask() {
        return task;
    }
}

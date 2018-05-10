package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public final class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isDone;

    @OneToOne(mappedBy = "issue")
    private Task task;

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

    public boolean getIsDone() {
        return isDone;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return String.format(
                "Issue[id=%d, description='%s', isDone='%s', task=%s]",
                id, description, isDone, task
        );
    }
}

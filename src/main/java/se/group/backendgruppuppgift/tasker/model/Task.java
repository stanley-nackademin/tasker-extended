package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public final class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    private User user;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(unique = true)
    private Issue issue;

    protected Task() {}

    public Task(String description, TaskStatus status) {
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return String.format(
                "Task[id=%d, description='%s', status='%s', user=%s, issue=%s]",
                id, description, status, user, issue
        );
    }
}

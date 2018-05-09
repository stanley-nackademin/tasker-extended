package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public final class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    private User user;

    @OneToOne
    @JoinColumn(unique = true)
    private Issue issue;

    protected Task() {}

    public Task(String name, TaskStatus status) {
        this.name = name;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Issue getIssue() {
        return issue;
    }
}

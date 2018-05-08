package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public class Task {

    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    private User user;
    @OneToOne(mappedBy = "task")
    private Issue issue;

    protected Task() {
    }

    public Task(String name, TaskStatus status) {
        this.name = name;
        this.status = TaskStatus.Unstarted;
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

    public User getUser() {
        return user;
    }
}

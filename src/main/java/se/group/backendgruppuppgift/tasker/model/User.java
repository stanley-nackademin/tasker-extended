package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public final class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userNumber;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Team team;

    @OneToMany(mappedBy = "user")
    private Collection<Task> tasks;

    protected User() {}

    public User(Long userNumber, String username, String firstName, String lastName, boolean isActive, Team team) {
        this.userNumber = userNumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = true;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public Long getUserNumber() {
        return userNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return isActive;
    }

    public Team getTeam() {
        return team;
    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, userNumber=%d, firstName='%s', lastName='%s', isActive=%s",
                id, userNumber, firstName, lastName, isActive
        );
    }
}

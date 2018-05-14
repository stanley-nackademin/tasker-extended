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

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Team team;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Collection<Task> tasks;

    protected User() {}

    public User(Long userNumber, String userName, String firstName, String lastName, Team team) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = true;
        this.team = team;
    }

    public Long getId() {
        return id;
    }

    public Long getUserNumber() {
        return userNumber;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isActive() {
        return active;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserNumber(Long userNumber) {
        this.userNumber = userNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, userNumber=%d, firstName='%s', lastName='%s', isActive=%s",
                id, userNumber, firstName, lastName, active
        );
    }
}

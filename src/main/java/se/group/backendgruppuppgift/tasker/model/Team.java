package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Team {

    @GeneratedValue
    @Id
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "team")
    private Collection<User> users;

    public Team(String name) {
        this.name = name;
        this.active = true;
    }

    protected Team() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public Collection<User> getUsers() {
        return users;
    }
}

package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
public final class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

//    @OneToMany(mappedBy = "team")
//    private Collection<User> users;

    @Column(nullable = false)
    private boolean isActive;

    protected Team(){}

    public Team(String name, boolean isActive) {
        this.name = name;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

//    public Collection<User> getUsers() {
//        return users;
//    }

    public boolean isActive() {
        return isActive;
    }
}

package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
<<<<<<< HEAD
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
=======
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
>>>>>>> 7f2547380ea8fe322f6dd4e602f4b10f615af10e
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

<<<<<<< HEAD
//    public Collection<User> getUsers() {
//        return users;
//    }

    public boolean isActive() {
        return isActive;
=======
    public boolean isActive() {
        return active;
    }

    public Collection<User> getUsers() {
        return users;
>>>>>>> 7f2547380ea8fe322f6dd4e602f4b10f615af10e
    }
}

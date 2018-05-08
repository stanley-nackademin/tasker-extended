package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public final class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userNumber;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private boolean isActive;

<<<<<<< HEAD
//    @ManyToOne
//    private Team team;
=======
    @ManyToOne
    private Team team;
>>>>>>> 7f2547380ea8fe322f6dd4e602f4b10f615af10e

    protected User() {}

    public User(Long userNumber, String username, String firstName, String lastName, boolean isActive) {
        this.userNumber = userNumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = isActive;
//        this.team = team;
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

//    public Team getTeam() {
//        return team;
//    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, userNumber=%d, firstName='%s', lastName='%s', isActive=%s",
                id, userNumber, firstName, lastName, isActive
        );
    }
}

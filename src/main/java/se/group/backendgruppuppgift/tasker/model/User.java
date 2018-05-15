package se.group.backendgruppuppgift.tasker.model;

import javax.persistence.*;

@Entity
public final class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userNumber;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    protected User() {}

    public User(Long userNumber, String username, String firstName, String lastName, Team team) {
        this.userNumber = userNumber;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public Team getTeam() {
        return team;
    }

    public void setUserNumber(Long userNumber) {
        this.userNumber = userNumber;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setIsActive(boolean active) {
        isActive = active;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, userNumber=%d, firstName='%s', lastName='%s', isActive=%s",
                id, userNumber, firstName, lastName, isActive
        );
    }
}

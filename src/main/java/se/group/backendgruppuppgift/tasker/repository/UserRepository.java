package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional
    Long removeByUserNumber(Long userNumber);

    Optional<User> findByUserNumber(Long userNumber);

    List<User> findUsersByTeamId(Long teamId);

    List<User> findByTeam(Team team);

    Optional<User> findFirstByOrderByUserNumberDesc();

    List<User> findUsersByFirstName(String firstName);

    List<User> findUsersByUsername(String userName);

    List<User> findUsersByLastName(String lastName);

    List<User> findUsersByFirstNameAndLastName(String firstName, String lastName);

    List<User> findUsersByFirstNameAndUsername(String firstName, String userName);

    List<User> findUsersByUsernameAndLastName(String userName, String lastName);

    List<User> findUsersByFirstNameAndLastNameAndUsername(String firstName, String lastName, String userName);

    Optional<User> findUserByUsername(String username);
}

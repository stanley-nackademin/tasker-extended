package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByTeam(Team team);

    Optional<User> findByUserNumber(Long userNumber);

    @Transactional
    Long removeByUserNumber (Long userNumber);

    List<User> findUsersByTeamId(Long teamId);

    List<User> findByTeam(Team team);

    //jonasmergeADD
    List<User> findUsersByTeam(Team team);
//    User findFirstByOrderByIdDesc();
    User findFirstByOrderByUserNumberDesc();

    List<User> findUsersByFirstName(String firstName);
    List<User> findUsersByUserName(String userName);
    List<User> findUsersByLastName(String lastName);
    List<User> findUsersByFirstNameAndLastName(String firstName, String lastName);
    List<User> findUsersByFirstNameAndUserName(String firstName, String userName);
    List<User> findUsersByUserNameAndLastName(String userName, String lastName);
    List<User> findUsersByFirstNameAndLastNameAndUserName(String firstName, String lastName, String userName);
    Optional<User> findByUserNumber(Long userNumber);
}

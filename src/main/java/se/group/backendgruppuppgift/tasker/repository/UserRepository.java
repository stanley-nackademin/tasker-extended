package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByTeam(Team team);

    Optional<User> findByUserNumber(Long userNumber);
}

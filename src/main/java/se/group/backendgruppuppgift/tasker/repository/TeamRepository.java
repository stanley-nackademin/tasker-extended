package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Team;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    List<Team> findAll();

    Optional<Team> findByName(String name);
}

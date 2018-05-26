package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}

package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
}

package se.group.backendgruppuppgift.tasker.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findAllByStatus(TaskStatus status);

    List<Task> findAllByNameContains(String name);

    //List<Task> findAllByIssueExists(); // TODO: 2018-05-09 Unsupported keyword 'EXISTS', returns boolean
}

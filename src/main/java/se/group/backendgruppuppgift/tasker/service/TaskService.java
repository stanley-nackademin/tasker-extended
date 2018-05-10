package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static se.group.backendgruppuppgift.tasker.model.TaskStatus.*;

@Service
public final class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskWeb createTask(TaskWeb task) {
        Task repoTask = repository.save(new Task(task.getName(), task.getStatus()));

        return convertToWeb(repoTask);
    }

    public Optional<TaskWeb> findTask(Long id) {
        Task repoTask = repository.findById(id).get();

        return Optional.of(convertToWeb(repoTask));
    }

    public List<TaskWeb> findTaskByStatus(String status) {

        status = prepareString(status);
        List<TaskWeb> result = new ArrayList<>();

        switch (status) {
            case "started":
                repository.findByStatus(STARTED).forEach(t -> result.add(convertToWeb(t)));
                return result;
            case "unstarted":
                repository.findByStatus(UNSTARTED).forEach(t -> result.add(convertToWeb(t)));
                return result;
            case "done":
                repository.findByStatus(DONE).forEach(t -> result.add(convertToWeb(t)));
                return result;
            default:
                return result;
        }
    }

    public Optional<TaskWeb> updateTask(Long id, TaskWeb task) {
        Optional<Task> result = repository.findById(id);

        if (result.isPresent()) {
            Task updatedTask = result.get();

            if (!isBlank(task.getName()))
                updatedTask.setName(task.getName());

            if (!isBlank(task.getStatus().toString()))
                updatedTask.setStatus(task.getStatus());

            return Optional.ofNullable(convertToWeb(repository.save(updatedTask)));
        }

        return Optional.empty();
    }

    public Optional<TaskWeb> deleteTask(Long id) {
        Optional<Task> repoTask = repository.findById(id);
        Optional<TaskWeb> result;

        if (repoTask.isPresent()) {
            repository.deleteById(id);
            result = Optional.ofNullable(convertToWeb(repoTask.get()));

            return result;
        }

        return Optional.empty();
    }

    private String prepareString(String string) {
        return string.trim().toLowerCase();
    }

    private TaskWeb convertToWeb(Task task) {
        return new TaskWeb(task.getId(), task.getName(), task.getStatus());
    }
}

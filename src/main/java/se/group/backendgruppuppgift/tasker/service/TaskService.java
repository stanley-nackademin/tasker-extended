package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isAllBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static se.group.backendgruppuppgift.tasker.model.TaskStatus.*;

@Service
public final class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public TaskWeb createTask(TaskWeb task) {
        Task repoTask = repository.save(new Task(task.getDescription(), task.getStatus()));

        return convertToWeb(repoTask);
    }

    public Optional<TaskWeb> findTask(Long id) {
        Optional<Task> repoTask = repository.findById(id);

        if (repoTask.isPresent())
            return Optional.ofNullable(convertToWeb(repoTask.get()));

        return Optional.empty();
    }

    public List<TaskWeb> findTasksByParams(String status, String team, String user, String text) {
        List<TaskWeb> result = new ArrayList<>();

        if (!isBlank(status) && isAllBlank(team, user, text)) {
            return findTasksByStatus(status);
        } else if (!isBlank(team) && isAllBlank(status, user, text)) {

        } else if (!isBlank(user) && isAllBlank(status, team, text)) {
            // TODO: 2018-05-11 Change to username
            repository.findByUserId(Long.parseLong(user)).forEach(t -> result.add(convertToWeb(t)));
        } else if (!isBlank(text) && isAllBlank(status, team, user)) {
            repository.findByDescriptionContains(text).forEach(t -> result.add(convertToWeb(t)));
        } else {
            repository.findAll().forEach(t -> result.add(convertToWeb(t)));
        }

        return result;
    }

    public Optional<TaskWeb> updateTask(Long id, TaskWeb task) {
        Optional<Task> repoTask = repository.findById(id);

        if (repoTask.isPresent()) {
            Task updatedTask = repoTask.get();

            if (!isBlank(task.getDescription()))
                updatedTask.setDescription(task.getDescription());

            if (!isBlank(task.getStatus().toString()))
                updatedTask.setStatus(task.getStatus());

            return Optional.ofNullable(convertToWeb(repository.save(updatedTask)));
        }

        return Optional.empty();
    }

    public Optional<TaskWeb> deleteTask(Long id) {
        Optional<Task> repoTask = repository.findById(id);

        if (repoTask.isPresent()) {
            repository.deleteById(id);

            return Optional.ofNullable(convertToWeb(repoTask.get()));
        }

        return Optional.empty();
    }

    private List<TaskWeb> findTasksByStatus(String status) {

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

    private String prepareString(String string) {
        return string.trim().toLowerCase();
    }

    private TaskWeb convertToWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus());
    }
}

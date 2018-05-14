package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTaskException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.*;
import static se.group.backendgruppuppgift.tasker.model.TaskStatus.*;

@Service
public final class TaskService {

    private final TaskRepository repository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public TaskWeb createTask(TaskWeb task) {
        validateTask(task);
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
            findTasksByStatus(status).forEach(t -> result.add(convertToWeb(t)));
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
        validateTask(task);
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

//    public Optional<TaskWeb> updateTaskUser(Long id, Long userNumber){
//        Optional<Task> task = repository.findById(id);
//        Optional<User> user = userRepository.findByUserNumber(userNumber);
//
//        Optional<TaskWeb> taskWeb = new TaskWeb();
//
//        if(task.isPresent()) {
//            Task updatedTask = task.get();
//        }
//
//    }

    public Optional<TaskWeb> deleteTask(Long id) {
        Optional<Task> repoTask = repository.findById(id);

        if (repoTask.isPresent()) {
            repository.deleteById(id);

            return Optional.ofNullable(convertToWeb(repoTask.get()));
        }

        return Optional.empty();
    }

    private List<Task> findTasksByStatus(String status) {

        status = prepareString(status);

        switch (status) {
            case "started":
                return repository.findByStatus(STARTED);
            case "unstarted":
                return repository.findByStatus(UNSTARTED);
            case "done":
                return repository.findByStatus(DONE);
            default:
                return new ArrayList<>();
        }
    }

//    private Optional<Task> updateTaskUser (Long id, Long userNumber){
//        Optional<Task> task = repository.findById(id);
//        Optional<User> user = userRepository.findByUserNumber(userNumber);
//
//        Task updatedTask = task.get();
//        User newUser = user.get();
//        updatedTask.setUser(newUser);
//
//        return repository.save(updatedTask);
//    }

    private String prepareString(String string) {
        return string.trim().toLowerCase();
    }

    private TaskWeb convertToWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus());
    }

    private void validateTask(TaskWeb task) {
        if (task.getStatus() == null || isBlank(task.getDescription()))
            throw new InvalidTaskException("Missing/invalid values");
    }
}

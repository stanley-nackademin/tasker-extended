package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Issue;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.IssueWeb;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.repository.IssueRepository;
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

    private final TaskRepository taskRepository;
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, IssueRepository issueRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task) {
        validateTask(task);
        Task taskResult = taskRepository.save(new Task(task.getDescription(), task.getStatus()));

        return taskResult;
    }

    public Optional<Task> findTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent())
            return Optional.ofNullable(task.get());

        return Optional.empty();
    }

    public List<Task> findTasksByParams(String status, String team, String user, String text, String value) {
        List<Task> result = new ArrayList<>();

        if (!isBlank(status) && isAllBlank(team, user, text, value)) {
            findTasksByStatus(status).forEach(t -> result.add(t));
        } else if (!isBlank(team) && isAllBlank(status, user, text, value)) {

        } else if (!isBlank(user) && isAllBlank(status, team, text, value)) {

            if (user.matches("[0-9]+")) {
                Optional<User> userRepo = userRepository.findByUserNumber(Long.parseLong(user));

                if (userRepo.isPresent()) {
                    User userObject = userRepo.get();
                    taskRepository.findByUserId(userObject.getId()).forEach(t -> result.add(t));
                }
            }
        } else if (!isBlank(text) && isAllBlank(status, team, user, value)) {
            taskRepository.findByDescriptionContains(text).forEach(t -> result.add(t));
        } else if (!isBlank(value) && value.equals("true") && isAllBlank(status, team, user, text)) {
            taskRepository.findByIssueNotNull().forEach(t -> result.add(t));
        } else if (!isBlank(value) && value.equals("false") && isAllBlank(status, team, user, text)) {
            taskRepository.findByIssueNull().forEach(t -> result.add(t));
        } else {
            taskRepository.findAll().forEach(t -> result.add(t));
        }

        return result;
    }

    public Optional<Task> updateTask(Long id, Task task) {
        validateTask(task);
        Optional<Task> taskResult = taskRepository.findById(id);

        if (taskResult.isPresent()) {
            Task updatedTask = taskResult.get();

            if (!isBlank(task.getDescription()))
                updatedTask.setDescription(task.getDescription());

            if (!isBlank(task.getStatus().toString()))
                updatedTask.setStatus(task.getStatus());

            return Optional.ofNullable(taskRepository.save(updatedTask));
        }

        return Optional.empty();
    }

    public Optional<Task> assignIssue(Long id, Issue issue) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task updatedTask = task.get();
            validateTaskStatus(updatedTask);
            Issue issueResult = issueRepository.save(new Issue(issue.getDescription()));

            updatedTask.setIssue(issueResult);
            updatedTask.setStatus(UNSTARTED);
            updatedTask = taskRepository.save(updatedTask);

            return Optional.ofNullable(updatedTask);
        }

        return Optional.empty();
    }

    public Optional<Task> deleteTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            taskRepository.deleteById(id);

            return Optional.ofNullable(task.get());
        }

        return Optional.empty();
    }

    private List<Task> findTasksByStatus(String status) {

        status = prepareString(status);

        switch (status) {
            case "started":
                return taskRepository.findByStatus(STARTED);
            case "unstarted":
                return taskRepository.findByStatus(UNSTARTED);
            case "done":
                return taskRepository.findByStatus(DONE);
            default:
                return new ArrayList<>();
        }
    }

    private String prepareString(String string) {
        return string.trim().toLowerCase();
    }

    private TaskWeb convertToWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus(), convertIssueToWeb(task.getIssue()));
    }

    private IssueWeb convertIssueToWeb(Issue issue) {
        return issue != null ? new IssueWeb(issue.getDescription(), issue.getIsDone()) : null;
    }

    private void validateTask(Task task) {
        if (task.getStatus() == null || isBlank(task.getDescription()))
            throw new InvalidTaskException("Missing/invalid values");
    }

    private void validateTaskStatus(Task task) {
        if (!(task.getStatus() == DONE))
            throw new InvalidTaskException("The current Task's status is not DONE");
    }
}

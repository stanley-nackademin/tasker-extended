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

    public TaskWeb createTask(TaskWeb taskWeb) {
        validateTask(taskWeb);
        Task task = taskRepository.save(new Task(taskWeb.getDescription(), taskWeb.getStatus()));

        return convertToWeb(task);
    }

    public Optional<TaskWeb> findTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent())
            return Optional.ofNullable(convertToWeb(task.get()));

        return Optional.empty();
    }

    public List<TaskWeb> findTasksByParams(String status, String team, String user, String text, String value) {
        List<TaskWeb> result = new ArrayList<>();

        if (!isBlank(status) && isAllBlank(team, user, text, value)) {
            findTasksByStatus(status).forEach(t -> result.add(convertToWeb(t)));
        } else if (!isBlank(team) && isAllBlank(status, user, text, value)) {

        } else if (!isBlank(user) && isAllBlank(status, team, text, value)) {

            if (user.matches("[0-9]+")) {
                Optional<User> userRepo = userRepository.findByUserNumber(Long.parseLong(user));

                if (userRepo.isPresent()) {
                    User userObject = userRepo.get();
                    taskRepository.findByUserId(userObject.getId()).forEach(t -> result.add(convertToWeb(t)));
                }
            }
        } else if (!isBlank(text) && isAllBlank(status, team, user, value)) {
            taskRepository.findByDescriptionContains(text).forEach(t -> result.add(convertToWeb(t)));
        } else if (!isBlank(value) && value.equals("true") && isAllBlank(status, team, user, text)) {
            taskRepository.findByIssueNotNull().forEach(t -> result.add(convertToWeb(t)));
        } else if (!isBlank(value) && value.equals("false") && isAllBlank(status, team, user, text)) {
            taskRepository.findByIssueNull().forEach(t -> result.add(convertToWeb(t)));
        } else {
            taskRepository.findAll().forEach(t -> result.add(convertToWeb(t)));
        }

        return result;
    }

    public Optional<TaskWeb> updateTask(Long id, TaskWeb taskWeb) {
        validateTask(taskWeb);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task updatedTask = task.get();

            if (!isBlank(taskWeb.getDescription()))
                updatedTask.setDescription(taskWeb.getDescription());

            if (!isBlank(taskWeb.getStatus().toString()))
                updatedTask.setStatus(taskWeb.getStatus());

            return Optional.ofNullable(convertToWeb(taskRepository.save(updatedTask)));
        }

        return Optional.empty();
    }

    public Optional<TaskWeb> assignIssue(Long id, IssueWeb issueWeb) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task updatedTask = task.get();
            validateTaskStatus(updatedTask);
            Issue issue = issueRepository.save(new Issue(issueWeb.getDescription()));

            updatedTask.setIssue(issue);
            updatedTask.setStatus(UNSTARTED);
            updatedTask = taskRepository.save(updatedTask);

            return Optional.ofNullable(convertToWeb(updatedTask));
        }

        return Optional.empty();
    }

    public Optional<TaskWeb> deleteTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            taskRepository.deleteById(id);

            return Optional.ofNullable(convertToWeb(task.get()));
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

    private void validateTask(TaskWeb task) {
        if (task.getStatus() == null || isBlank(task.getDescription()))
            throw new InvalidTaskException("Missing/invalid values");
    }

    private void validateTaskStatus(Task task) {
        if (!(task.getStatus() == DONE))
            throw new InvalidTaskException("The current Task's status is not DONE");
    }
}

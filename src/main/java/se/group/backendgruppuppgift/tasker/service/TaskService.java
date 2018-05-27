package se.group.backendgruppuppgift.tasker.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Issue;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.IssueRepository;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidIssueException;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTaskException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static org.apache.commons.lang3.StringUtils.isAllBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static se.group.backendgruppuppgift.tasker.model.TaskStatus.*;

@Service
public final class TaskService {

    private static final int PAGE_SIZE = 10;

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

        return taskRepository.save(new Task(task.getDescription(), task.getStatus()));
    }

    public Optional<Task> findTask(Long id) {
        return taskRepository.findById(id);
    }

    public List<Task> findTasksByParams(String status, String team, String user, String text, String issue,
                                        String page, String startDate, String endDate) {
        List<Task> result;

        if (!isBlank(status) && isAllBlank(team, user, text, issue, page, startDate, endDate)) {
            result = findTasksByStatus(status);
        } else if (!isBlank(team) && isAllBlank(status, user, text, issue, page, startDate, endDate)) {
            result = findByTeamId(team);
        } else if (!isBlank(user) && isAllBlank(status, team, text, issue, page, startDate, endDate)) {
            result = findByUserNumber(user);
        } else if (!isBlank(text) && isAllBlank(status, team, user, issue, page, startDate, endDate)) {
            result = taskRepository.findByDescriptionContains(text);
        } else if (!isBlank(issue) && issue.equals("true") && isAllBlank(status, team, user, text, page, startDate, endDate)) {
            result = taskRepository.findByIssueNotNull();
        } else if (!isBlank(issue) && issue.equals("false") && isAllBlank(status, team, user, text, page, startDate, endDate)) {
            result = taskRepository.findByIssueNull();
        } else if (!isBlank(page) && isAllBlank(status, team, user, text, issue, startDate, endDate)) {
            if (isDigit(page)) {
                result = taskRepository.findAll(PageRequest.of(Integer.parseInt(page), PAGE_SIZE)).getContent();
            } else {
                result = taskRepository.findAll(PageRequest.of(0, PAGE_SIZE)).getContent();
            }
        } else if (!isAllBlank(startDate, endDate) && isAllBlank(status, team, user, text, issue, page)) {
            validateDate(startDate);
            validateDate(endDate);

            LocalDate localStartDate = LocalDate.parse(startDate, ISO_DATE);
            LocalDate localEndDate = LocalDate.parse(endDate, ISO_DATE);
            validateDateRange(localStartDate, localEndDate);

            result = taskRepository.findAllByFinishDateBetween(localStartDate, localEndDate);
        } else {
            result = taskRepository.findAll();
        }

        return result;
    }

    public Optional<Task> updateTask(Long id, Task task) {
        Optional<Task> taskResult = taskRepository.findById(id);

        if (taskResult.isPresent()) {
            Task updatedTask = taskResult.get();

            if (!isBlank(task.getDescription())) {
                updatedTask.setDescription(task.getDescription());
            }

            if (task.getStatus() != null) {
                if (!updatedTask.getStatus().equals(DONE) && task.getStatus().equals(DONE)) {
                    updatedTask.setFinishDate(LocalDate.now());
                } else if (updatedTask.getStatus().equals(DONE) && !task.getStatus().equals(DONE)) {
                    updatedTask.setFinishDate(null);
                }
                updatedTask.setStatus(task.getStatus());
            }

            return Optional.of(taskRepository.save(updatedTask));
        }

        return taskResult;
    }

    public Optional<Task> assignIssue(Long id, Issue issue) {
        validateIssue(issue);
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            Task updatedTask = task.get();
            validateTaskStatus(updatedTask);
            Issue issueResult = issueRepository.save(new Issue(issue.getDescription()));

            updatedTask.setIssue(issueResult);
            updatedTask.setStatus(UNSTARTED);
            updatedTask = taskRepository.save(updatedTask);

            return Optional.of(updatedTask);
        }

        return task;
    }

    public Optional<Task> deleteTask(Long id) {
        Optional<Task> task = taskRepository.findById(id);

        if (task.isPresent()) {
            taskRepository.deleteById(id);
        }

        return task;
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

    private void validateTask(Task task) {
        if (task.getStatus() == null || isBlank(task.getDescription())) {
            throw new InvalidTaskException("Missing/invalid values");
        }
    }

    private void validateTaskStatus(Task task) {
        if (task.getStatus() != DONE) {
            throw new InvalidTaskException("The current Task's status is not DONE");
        }
    }

    private void validateIssue(Issue issue) {
        if (isBlank(issue.getDescription())) {
            throw new InvalidIssueException("Description can not be empty");
        }
    }

    private void validateDate(String date) {
        if (!date.matches("^(19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")) {
            throw new InvalidTaskException("Invalid date");
        }
    }

    private void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidTaskException("Invalid date range");
        }
    }

    private boolean isDigit(String number) {
        return number.matches("[0-9]+");
    }

    private List<Task> findByTeamId(String id) {
        List<Task> result = new ArrayList<>();

        if (id.matches("[0-9]+")) {
            List<User> users = userRepository.findUsersByTeamId(Long.parseLong(id));

            for (User u : users) {
                result.addAll(taskRepository.findByUserId(u.getId()));
            }
        }

        return result;
    }

    private List<Task> findByUserNumber(String userNumber) {
        List<Task> result = new ArrayList<>();

        if (userNumber.matches("[0-9]+")) {
            Optional<User> user = userRepository.findByUserNumber(Long.parseLong(userNumber));

            if (user.isPresent()) {
                result = taskRepository.findByUserId(user.get().getId());
            }
        }

        return result;
    }
}

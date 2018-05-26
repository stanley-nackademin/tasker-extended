package se.group.backendgruppuppgift.tasker.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.*;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTaskException;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTeamException;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidUserException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.commons.lang3.StringUtils.isAllBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public final class UserService {

    private static final int PAGE_SIZE = 10;

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;

    public UserService(UserRepository userRepository, TeamRepository teamRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.taskRepository = taskRepository;
    }

    public User createUser(User user) {
        validateUsername(user.getUsername());

        Long userNumber;
        Optional<User> result = userRepository.findFirstByOrderByUserNumberDesc();

        if (result.isPresent()) {
            userNumber = result.get().getUserNumber();
        } else {
            userNumber = 1000L;
        }

        AtomicLong number = new AtomicLong(userNumber);
        userNumber = number.incrementAndGet();

        user.setUserNumber(userNumber);
        user.setIsActive(true);

        return userRepository.save(user);
    }

    public Optional<User> findUserByUserNumber(Long userNumber) {
        return userRepository.findByUserNumber(userNumber);
    }

    public User findLastUser() {
        return userRepository.findFirstByOrderByUserNumberDesc().get();
    }

    public List<User> findAllUsersBy(String firstName, String lastName, String userName, String page) {
        if (!isBlank(firstName) && isAllBlank(lastName, userName, page)) {
            return userRepository.findUsersByFirstName(firstName);
        } else if (!isBlank(lastName) && isAllBlank(firstName, userName, page)) {
            return userRepository.findUsersByLastName(lastName);
        } else if (!isBlank(userName) && isAllBlank(firstName, lastName, page)) {
            return userRepository.findUsersByUsername(userName);
        } else if (!isAllBlank(firstName, lastName) && isAllBlank(userName, page)) {
            return userRepository.findUsersByFirstNameAndLastName(firstName, lastName);
        } else if (!isAllBlank(firstName, userName) && isAllBlank(lastName, page)) {
            return userRepository.findUsersByFirstNameAndUsername(firstName, userName);
        } else if (!isAllBlank(lastName, userName) && isAllBlank(firstName, page)) {
            return userRepository.findUsersByUsernameAndLastName(userName, lastName);
        } else if (!isBlank(page) && isAllBlank(firstName, lastName, userName)) {
            if (isDigit(page)) {
                return userRepository.findAll(PageRequest.of(Integer.parseInt(page), PAGE_SIZE)).getContent();
            } else {
                return userRepository.findAll(PageRequest.of(0, PAGE_SIZE)).getContent();
            }
        } else if (!isAllBlank(firstName, lastName, userName) && isBlank(page)) {
            return userRepository.findUsersByFirstNameAndLastNameAndUsername(firstName, lastName, userName);
        } else {
            return userRepository.findAll();
        }
    }

    public Optional<User> userActivator(Long userNumber, Action action) {
        Optional<User> result = userRepository.findByUserNumber(userNumber);

        if (action.getAction().equalsIgnoreCase("activate")) {
            if (result.isPresent()) {
                User newUser = result.get();
                newUser.setIsActive(!newUser.getIsActive());

                if (!newUser.getIsActive()) {
                    List<Task> tasks = taskRepository.findAllByUserUserNumber(newUser.getUserNumber());

                    for (Task t : tasks) {
                        t.setStatus(TaskStatus.UNSTARTED);
                    }
                    taskRepository.saveAll(tasks);
                }
                userRepository.save(newUser);
            }
        }

        return result;
    }

    public Optional<User> updateUser(Long userNumber, User user) {
        Optional<User> result = userRepository.findByUserNumber(userNumber);

        if (result.isPresent()) {
            User updatedUser = result.get();

            if (!isBlank(user.getFirstName())) {
                updatedUser.setFirstName(user.getFirstName());
            }

            if (!isBlank(user.getLastName())) {
                updatedUser.setLastName(user.getLastName());
            }

            if (user.getIsActive() != null) {
                updatedUser.setIsActive(user.getIsActive());
            }

            if (user.getTeam() != null && !isBlank(user.getTeam().getId().toString())) {
                updatedUser.setTeam(user.getTeam());
            }

            return Optional.of(userRepository.save(updatedUser));
        }

        return result;
    }

    public Optional<Team> assignTeam(Long id, User user) {
        Optional<User> optionalUser = userRepository.findByUserNumber(user.getUserNumber());

        if (optionalUser.isPresent()) {
            User userResult = optionalUser.get();
            userTeamValidation(userResult);

            Optional<Team> optionalTeam = teamRepository.findById(id);

            if (optionalTeam.isPresent()) {
                Team team = optionalTeam.get();
                maxUserLimitValidation(team);
                userResult.setTeam(team);

                userRepository.save(userResult);

                return Optional.of(team);
            }
        }

        return Optional.empty();
    }

    public Optional<Task> assignTaskToUser(Long userNumber, Long taskId) {
        Optional<User> user = userRepository.findByUserNumber(userNumber);
        Optional<Task> task = taskRepository.findById(taskId);

        if (task.isPresent() && user.isPresent()) {
            User userResult = user.get();
            Task taskResult = task.get();

            validateTaskHasNoUser(taskResult);
            validateUserIsActive(userResult);
            validateTotalTasks(userResult.getUserNumber());

            taskResult.setUser(userResult);

            return Optional.of(taskRepository.save(taskResult));
        }

        return task;
    }

    public Optional<User> deleteUserByUserNumber(Long userNumber) {
        Optional<User> user = findUserByUserNumber(userNumber);

        if (user.isPresent()) {
            userRepository.removeByUserNumber(userNumber);
        }

        return user;
    }

    private void maxUserLimitValidation(Team team) {
        List<User> users = userRepository.findByTeam(team);

        if (users.size() >= 10) {
            throw new InvalidTeamException("Team: " + team.getName() + " is full, max 10 users in a team");
        }
    }

    private void userTeamValidation(User user) {
        if (user.getTeam() != null) {
            throw new InvalidTeamException("User: " + user.getUsername() + " is already in a team");
        }
    }

    private void validateUsername(String username) {
        if (isBlank(username) || username.length() <= 10) {
            throw new InvalidUserException("Username needs to be 10 characters or longer!");
        }

        if (userRepository.findUserByUsername(username).isPresent()) {
            throw new InvalidUserException("Username is already in use");
        }
    }

    private void validateTotalTasks(Long userNumber) {
        List<Task> tasks = taskRepository.findAllByUserUserNumber(userNumber);

        if (tasks.size() >= 5) {
            throw new InvalidUserException("The user has already 5 tasks");
        }
    }

    private void validateUserIsActive(User user) {
        if (!user.getIsActive()) {
            throw new InvalidUserException("This user is not active");
        }
    }

    private void validateTaskHasNoUser(Task task) {
        if (task.getUser() != null) {
            throw new InvalidTaskException("Task already has a user assigned");
        }
    }

    private boolean isDigit(String number) {
        return number.matches("[0-9]+");
    }
}

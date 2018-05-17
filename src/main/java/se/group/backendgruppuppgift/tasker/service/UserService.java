package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTaskException;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTeamException;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidUserException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.apache.commons.lang3.StringUtils.isBlank;


@Service
public final class UserService {

    private final UserRepository repository;
    private final TeamRepository teamRepository;
    private final TaskRepository taskRepository;

    public UserService(UserRepository repository, TeamRepository teamRepository, TaskRepository taskRepository) {
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.taskRepository = taskRepository;
    }

    public User createUser(User user) {
        Long userNumber;
        Optional<User> optionalUser = repository.findFirstByOrderByUserNumberDesc();

        if (optionalUser.isPresent()) {
            userNumber = repository.findFirstByOrderByUserNumberDesc().get().getUserNumber();
        } else {
            userNumber = 1000L;
        }

        AtomicLong number = new AtomicLong(userNumber);
        userNumber = number.incrementAndGet();

        user.setUserNumber(userNumber);
        user.setIsActive(true);

        if (repository.findUserByUsername(user.getUsername()).isPresent()) {
            throw new InvalidUserException("Username is already in use");
        }

        checkUsername(user);
        repository.save(user);

        return user;
    }

    public Optional<User> findUserByUserNumber(Long userNumber) {
        Optional<User> user = repository.findByUserNumber(userNumber);

        if (user.isPresent()) {
            return user;
        }

        return Optional.empty();
    }

    public User findLastUser() {
        return repository.findFirstByOrderByUserNumberDesc().get();
    }

    public List<User> findAllUsersBy(String firstName, String lastName, String userName) {
        if (!firstName.isEmpty() && lastName.isEmpty() && userName.isEmpty()) {
            return repository.findUsersByFirstName(firstName);
        } else if (firstName.isEmpty() && !lastName.isEmpty() && userName.isEmpty()) {
            return repository.findUsersByLastName(lastName);
        } else if (firstName.isEmpty() && lastName.isEmpty() && !userName.isEmpty()) {
            return repository.findUsersByUsername(userName);
        } else if (!firstName.isEmpty() && !lastName.isEmpty() && userName.isEmpty()) {
            return repository.findUsersByFirstNameAndLastName(firstName, lastName);
        } else if (!firstName.isEmpty() && lastName.isEmpty() && !userName.isEmpty()) {
            return repository.findUsersByFirstNameAndUsername(firstName, userName);
        } else if (firstName.isEmpty() && !lastName.isEmpty() && !userName.isEmpty()) {
            return repository.findUsersByUsernameAndLastName(userName, lastName);
        } else {
            return repository.findUsersByFirstNameAndLastNameAndUsername(firstName, lastName, userName);
        }
    }

    public User userActivator(Long userNumber) {
        Optional<User> newUserOpt = repository.findByUserNumber(userNumber);
        User newUser = newUserOpt.get();
        newUser.setIsActive(newUser.getIsActive() == true ? false : true);

        if (newUser.getIsActive() == false) {
            List<Task> task = taskRepository.findAllByUserUserNumber(newUser.getUserNumber());

            for (Task t : task) {
                t.setStatus(TaskStatus.UNSTARTED);
                t.setUser(null);
                taskRepository.save(t);
            }
        }

        return repository.save(newUser);
    }

    public Optional<User> updateUser(Long userNumber, User user) {
        Optional<User> result = repository.findByUserNumber(userNumber);

        if (result.isPresent()) {
            User updatedUser = result.get();

            if (!isBlank(user.getFirstName()))
                updatedUser.setFirstName(user.getFirstName());
            if (!isBlank(user.getLastName()))
                updatedUser.setLastName(user.getLastName());
            if (user.getIsActive() != null)
                updatedUser.setIsActive(user.getIsActive());
            if (user.getTeam() != null && !isBlank(user.getTeam().getId().toString()))
                updatedUser.setTeam(user.getTeam());

            return Optional.ofNullable(repository.save(updatedUser));
        }

        return Optional.empty();
    }

    public Optional<Team> addTeam(Long id, User user) {
        Optional<User> optionalUser = repository.findByUserNumber(user.getUserNumber());
        Optional<Team> optionalTeam = teamRepository.findById(id);

        if (optionalUser.isPresent()) {
            User userResult = optionalUser.get();
            userTeamValidation(userResult);

            if (optionalTeam.isPresent()) {
                Team team = optionalTeam.get();
                maxUserLimitValidation(team);
                userResult.setTeam(team);

                repository.save(userResult);

                return Optional.ofNullable(team);
            }
        }

        return Optional.empty();
    }

    public Optional<Task> assignTaskToUser(Long userNumber, Long taskId) {
        Optional<User> user = repository.findByUserNumber(userNumber);
        Optional<Task> task = taskRepository.findById(taskId);

        if (task.isPresent() && user.isPresent()) {
            User userResult = user.get();
            Task taskResult = task.get();

            notHasUser(taskResult);
            isUserActive(userResult);
            canAddTasks(userResult.getUserNumber());

            taskResult.setUser(userResult);

            return Optional.ofNullable(taskRepository.save(taskResult));
        }

        return Optional.empty();
    }

    public Optional<User> deleteUserByUserNumber(Long userNumber) {
        Optional<User> user = findUserByUserNumber(userNumber);

        if (user.isPresent()) {
            repository.removeByUserNumber(userNumber);
        }

        return user;
    }

    private void maxUserLimitValidation(Team team) {
        List<User> users = repository.findByTeam(team);

        if (users.size() >= 10) {
            throw new InvalidTeamException("Team: " + team.getName() + " is full, max 10 users in a team");
        }
    }

    private void userTeamValidation(User user) {
        if (user.getTeam() != null) {
            throw new InvalidTeamException("User: " + user.getUsername() + " is already in a team");
        }
    }

    private void checkUsername(User user) {
        if (user.getUsername() == null || user.getUsername().length() <= 10) {
            throw new InvalidUserException("Username needs to be 10 characters or longer!");
        }
    }

    private void canAddTasks(Long userNumber) {
        List<Task> tasks = taskRepository.findAllByUserUserNumber(userNumber);

        if (tasks.size() >= 5) {
            throw new InvalidUserException("The user has already 5 tasks");
        }
    }

    private void isUserActive(User user) {
        if (!user.getIsActive()) {
            throw new InvalidUserException("This user is not active");
        }
    }

    private void notHasUser(Task task) {
        if (task.getUser() != null) {
            throw new InvalidTaskException("Task already has a user assigned");
        }
    }
}

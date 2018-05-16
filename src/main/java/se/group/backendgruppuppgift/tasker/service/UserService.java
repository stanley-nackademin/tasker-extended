package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.*;
import se.group.backendgruppuppgift.tasker.model.web.*;
import se.group.backendgruppuppgift.tasker.repository.*;
import se.group.backendgruppuppgift.tasker.service.exception.*;

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

    //CREATE

    public User createUser(User user) {
        Long userNumber;
        Optional<User> optionalUser = repository.findFirstByOrderByUserNumberDesc();

        if (optionalUser.isPresent()){
            userNumber = repository.findFirstByOrderByUserNumberDesc().get().getUserNumber();
        }else{
            userNumber = 1000L;
        }

        AtomicLong number = new AtomicLong(userNumber);
        userNumber = number.incrementAndGet();

        user.setUserNumber(userNumber);
        user.setIsActive(true);

        if (repository.findUserByUsername(user.getUsername()).isPresent()){
            throw new InvalidUserException("Username already in use");
        }

        checkUsername(user);
        repository.save(user);
        return user;
    }

    //READ

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

    public List<User> findUsersByTeamId(Long teamId) {
        return repository.findUsersByTeamId(teamId);
    }

    public List<User> findAllUsersBy(String firstName, String lastName, String userName) {
        System.out.println(firstName + lastName + userName);
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

    //UPDATE

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

    public Optional<TaskWeb> updateUserTask(Long userNumber, Long taskId) {
        Optional<User> userResult = repository.findByUserNumber(userNumber);
        Optional<Task> taskResult = taskRepository.findById(taskId);

        if (taskResult.isPresent()) {
            Task updatedTask = taskResult.get();
            User updatedUser = userResult.get();
            updatedTask.setUser(updatedUser);
            updatedTask.setStatus(TaskStatus.STARTED);
            return Optional.ofNullable(convertTaskToWeb(taskRepository.save(updatedTask)));
        }
        return Optional.empty();
    }

    public Optional<TeamWeb> addTeam(Long id, UserWeb userWeb) {
        Optional<User> result = repository.findByUserNumber(userWeb.getUserNumber());
        Optional<Team> resultTeam = teamRepository.findById(id);

        if (result.isPresent()) {
            User user = result.get();
            userTeamValidation(user);

            if (resultTeam.isPresent()) {
                Team team = resultTeam.get();
                maxUserLimitValidation(team);
                user.setTeam(team);

                repository.save(user);
                return Optional.ofNullable(convertToTeamWeb(team));
            }
        }
        return Optional.empty();
    }

    //DELETE

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

    private TeamWeb convertToTeamWeb(Team team) {
        return new TeamWeb(team.getId(), team.getName(), team.getIsActive());
    }

    private TaskWeb convertTaskToWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus(), null);
    }

    private void checkUsername(User user) {
        if (user.getFirstName() == null || user.getFirstName().length() <= 10) {
            throw new InvalidUserException("FirstName needs to be 10 characters or longer!");
        }
    }
}

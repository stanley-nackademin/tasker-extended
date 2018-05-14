package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidUserException;

import java.util.List;
import java.util.Optional;

@Service
public final class UserService {

    private final UserRepository repository;
    private final TaskRepository taskRepository;

    public UserService(UserRepository repository, TaskRepository taskRepository) {
        this.repository = repository;
        this.taskRepository = taskRepository;
    }

    public User createUser(User user) {
        checkUserName(user);
        return repository.save(user);
    }

    public Optional<User> findUserByUserNumber(Long userNumber){
        return repository.findByUserNumber(userNumber);
    }

    public Optional<User> deleteUserByUserNumber(Long userNumber){
            Optional<User> user = findUserByUserNumber(userNumber);
            if(user.isPresent()){
            repository.removeByUserNumber(userNumber);
        }
        return user;
    }

    public List<User> findUsersByTeamId(Long teamId){
        return repository.findUsersByTeamId(teamId);
    }

    public User updateUser(User newUser){
        return repository.save(newUser);
    }

    public List<User> findAllUsersBy(String firstName, String lastName, String userName){
        System.out.println(firstName + lastName + userName);
        if (!firstName.isEmpty() && lastName.isEmpty() && userName.isEmpty()){ // om allt är null förutom firstname
           return repository.findUsersByFirstName(firstName);
        }else if(firstName.isEmpty() && !lastName.isEmpty() && userName.isEmpty()){ // om allt är null förutom lastname
            return repository.findUsersByLastName(lastName);
        }else if(firstName.isEmpty() && lastName.isEmpty() && !userName.isEmpty()){ // om allt är null förutom username
            return repository.findUsersByUsername(userName);
        }else if (!firstName.isEmpty() && !lastName.isEmpty() && userName.isEmpty()){ // om allt är null förutom firsntame och lastname
            return repository.findUsersByFirstNameAndLastName(firstName,lastName);
        }else if (!firstName.isEmpty() && lastName.isEmpty() && !userName.isEmpty()){ // om allt är null förutom firstNAme och username
            return repository.findUsersByFirstNameAndUsername(firstName,userName);
        }else if(firstName.isEmpty() && !lastName.isEmpty() && !userName.isEmpty()){ // om firstname är null
            return repository.findUsersByUsernameAndLastName(userName, lastName);
        }else
            {return repository.findUsersByFirstNameAndLastNameAndUsername(firstName, lastName, userName);
        }
    }

    // -------------------------------TODO DENNA ÄR INTE KLAR.
    public Optional<TaskWeb> updateUserTask(Long userNumber, Long taskId){
        Optional<User> userResult = repository.findByUserNumber(userNumber);
        Optional<Task> taskResult = taskRepository.findById(taskId);

        if(taskResult.isPresent()){
           Task updatedTask = taskResult.get();
           User updatedUser = userResult.get();
           updatedTask.setUser(updatedUser);
           return Optional.ofNullable(convertTaskToWeb(taskRepository.save(updatedTask)));
        }
        return Optional.empty();
    }

    private UserWeb convertToWeb(User user) {
        return new UserWeb(user.getUserNumber(),user.getUsername(),user.getFirstName(),user.getLastName(),user.getIsActive(),user.getTeam());
    }

    private TaskWeb convertTaskToWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus());
    }

    private void checkUserName(User user){
        if(user.getFirstName() == null || user.getFirstName().length() <= 10){
            throw new InvalidUserException("FirstName needs to be 10 characters or longer!");
        }
    }
}

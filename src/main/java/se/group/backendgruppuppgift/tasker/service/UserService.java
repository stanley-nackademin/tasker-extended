package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.TaskStatus;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.model.web.TeamWeb;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.repository.TaskRepository;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTeamException;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidUserException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public final class UserService {



    private final UserRepository repository;
    private  final TeamRepository teamRepository;
    private final TaskRepository taskRepository;

    public UserService(UserRepository repository, TeamRepository teamRepository, TaskRepository taskRepository) {
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.taskRepository = taskRepository;
    }

    public UserWeb createUser(UserWeb user) {
        Long userNumber;
        try{
            userNumber = repository.findFirstByOrderByUserNumberDesc().getUserNumber();
        }catch (NullPointerException e){
            userNumber = 1000L;
        }

        AtomicLong number = new AtomicLong(userNumber);
        userNumber = number.incrementAndGet();

        UserWeb userWeb = new UserWeb(userNumber, user.getusername() ,user.getFirstName(), user.getLastName(), user.getTeam());
        User entityUser = new User(userWeb.getUserNumber(), userWeb.getusername(), userWeb.getFirstName(), userWeb.getLastName(), null);
        repository.save(entityUser);
        return userWeb;
    }

//        public Optional<User> findByUserNumber(Long userNumber){
//            return repository.findByUserNumber(userNumber);
//        }

    public Optional<UserWeb> findUserByUserNumber(Long userNumber){
        Optional<User> user = repository.findByUserNumber(userNumber);
        if(user.isPresent()){
            User user2 = user.get();
            return UserWeb.getOptionalFromUser(user2);
        }
        return Optional.empty();
    }

    public Optional<UserWeb> deleteUserByUserNumber(Long userNumber){
        Optional<UserWeb> user = findUserByUserNumber(userNumber);
        if(user.isPresent()){
    //public Optional<User> deleteUserByUserNumber(Long userNumber){
    //        Optional<User> user = findUserByUserNumber(userNumber);
            //       if(user.isPresent()){
            repository.removeByUserNumber(userNumber);
        }
        return user;
    }

    public User findLastUser(){
        return repository.findFirstByOrderByUserNumberDesc();
    }

    public List<User> findUsersByTeamId(Long teamId){
        return repository.findUsersByTeamId(teamId);
    }

    public User userActivator(Long userNumber){
        Optional<User> newUserOpt = repository.findByUserNumber(userNumber);
        User newUser = newUserOpt.get();
        if(newUser.getIsActive() == false){
            //Lägg till kod för att ta ta bort user från Task och ändra status till Unstarted
            List<Task> task = taskRepository.findAllByUserUserNumber(newUser.getUserNumber());
            for(Task t: task){
                t.setStatus(TaskStatus.UNSTARTED);
                t.setUser(null);
                taskRepository.save(t);
            }
        }
        return repository.save(newUser);
    }

    public User updateUser(User user){
        return repository.save(user);
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

    public List<User> findAllUsersBy(String firstName, String lastName, String username){
        System.out.println(firstName + lastName + username);
        if (!firstName.isEmpty() && lastName.isEmpty() && username.isEmpty()){ // om allt är null förutom firstname
           return repository.findUsersByFirstName(firstName);
        }else if(firstName.isEmpty() && !lastName.isEmpty() && username.isEmpty()){ // om allt är null förutom lastname
            return repository.findUsersByLastName(lastName);
        }else if(firstName.isEmpty() && lastName.isEmpty() && !username.isEmpty()){ // om allt är null förutom username
            return repository.findUsersByUsername(username);
        }else if (!firstName.isEmpty() && !lastName.isEmpty() && username.isEmpty()){ // om allt är null förutom firsntame och lastname
            return repository.findUsersByFirstNameAndLastName(firstName,lastName);
        }else if (!firstName.isEmpty() && lastName.isEmpty() && !username.isEmpty()){ // om allt är null förutom firstNAme och username
            return repository.findUsersByFirstNameAndUsername(firstName,username);
        }else if(firstName.isEmpty() && !lastName.isEmpty() && !username.isEmpty()){ // om firstname är null
            return repository.findUsersByUsernameAndLastName(username, lastName);
        }else
            {return repository.findUsersByFirstNameAndLastNameAndUsername(firstName, lastName, username);
        }
    }

    public Optional<TeamWeb> addTeam(Long id, UserWeb userWeb){
        Optional<User> result = repository.findByUserNumber(userWeb.getUserNumber());
        Optional<Team> resultTeam = teamRepository.findById(id);

        if(result.isPresent()){
            User user = result.get();
            userTeamValidation(user);

            if(resultTeam.isPresent()){
                Team team = resultTeam.get();
                maxUserLimitValidation(team);
                user.setTeam(team);

                repository.save(user);
                return Optional.ofNullable(convertToTeamWeb(team));
            }
        }
        return Optional.empty();
    }

    private void maxUserLimitValidation(Team team) {
        List<User> users = repository.findByTeam(team);

        if (users.size() >= 10) {
            throw new InvalidTeamException("Team: " + team.getName() + " is full, max 10 users in a team");
        }
    }

    private void userTeamValidation(User user) {

        if (user.getTeam() != null) {
            throw new InvalidTeamException("User: " + user.getuser() + " is already in a team");
        }

    }

    private TeamWeb convertToTeamWeb(Team team) {
        return new TeamWeb(team.getId(), team.getName(), team.getIsActive());
    }

    private UserWeb convertToWeb(User user) {
        return new UserWeb(user.getUserNumber(),user.getusername(),user.getFirstName(),user.getLastName(),user.getIsActive(),user.getTeam());
    }

    private TaskWeb convertTaskToWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus());
    }

    private void checkusername(User user){
        if(user.getFirstName() == null || user.getFirstName().length() <= 10){
            throw new InvalidUserException("FirstName needs to be 10 characters or longer!");
        }
    }
}

package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public final class UserService {



    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
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

        UserWeb userWeb = new UserWeb(userNumber, user.getUserName() ,user.getFirstName(), user.getLastName());
        User entityUser = new User(userWeb.getUserNumber(), userWeb.getUserName(), userWeb.getFirstName(), userWeb.getLastName(), null);
        repository.save(entityUser);
        return userWeb;
    }

    public Optional<UserWeb> findUserByUserNumber(Long userNumber){
        Optional<User> user = repository.findUserByUserNumber(userNumber);
        if(user.isPresent()){
            User user2 = user.get();
            return UserWeb.getOptionalFromUser(user2);
        }
        return Optional.empty();
    }

    public Optional<UserWeb> deleteUserByUserNumber(Long userNumber){
        Optional<UserWeb> user = findUserByUserNumber(userNumber);
        if(user.isPresent()){
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

    public User updateUser( User newUser){
        return repository.save(newUser);
    }

    public List<User> findAllUsersBy(String firstName, String lastName, String userName){
        System.out.println(firstName + lastName + userName);
        if (!firstName.isEmpty() && lastName.isEmpty() && userName.isEmpty()){ // om allt är null förutom firstname
           return repository.findUsersByFirstName(firstName);
        }else if(firstName.isEmpty() && !lastName.isEmpty() && userName.isEmpty()){ // om allt är null förutom lastname
            return repository.findUsersByLastName(lastName);
        }else if(firstName.isEmpty() && lastName.isEmpty() && !userName.isEmpty()){ // om allt är null förutom username
            return repository.findUsersByUserName(userName);
        }else if (!firstName.isEmpty() && !lastName.isEmpty() && userName.isEmpty()){ // om allt är null förutom firsntame och lastname
            return repository.findUsersByFirstNameAndLastName(firstName,lastName);
        }else if (!firstName.isEmpty() && lastName.isEmpty() && !userName.isEmpty()){ // om allt är null förutom firstNAme och username
            return repository.findUsersByFirstNameAndUserName(firstName,userName);
        }else if(firstName.isEmpty() && !lastName.isEmpty() && !userName.isEmpty()){ // om firstname är null
            return repository.findUsersByUserNameAndLastName(userName, lastName);
        }else
            {return repository.findUsersByFirstNameAndLastNameAndUserName(firstName, lastName, userName);
        }
    }
}

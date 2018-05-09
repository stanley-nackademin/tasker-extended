package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public final class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(User user) {
        return repository.save(user);
    }

    public Optional<User> findUserByUserNumber(Long userNumber){
        return repository.findUserByUserNumber(userNumber);
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
}

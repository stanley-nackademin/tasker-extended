package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;

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

    public Optional<User> findUser(Long userNumber){
        return repository.findUserByUserNumber(userNumber);
    }

    public Optional<User> deleteUser(Long userNumber){
        Optional<User> user = findUser(userNumber);

        if(user.isPresent())
            repository.removeByUserNumber(userNumber);
        return user;
    }
}

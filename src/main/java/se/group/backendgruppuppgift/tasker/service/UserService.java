package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;

import java.util.ArrayList;
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

    public User getUser(Long id){
        validate(id);
        return repository.findById(id).orElse(null);
    }

    private void validate(Long id) {
            System.out.println(repository.findById(id));
        if(!repository.findById(id).isPresent()){
            throw new InvalidTeamException("Could not 2: ");
        }
    }
}

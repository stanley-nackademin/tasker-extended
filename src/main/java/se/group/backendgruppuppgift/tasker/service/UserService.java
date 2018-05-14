package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.TeamWeb;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTeamException;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public final class UserService {

    private final UserRepository repository;
    private  final TeamRepository teamRepository;

    public UserService(UserRepository repository, TeamRepository teamRepository) {
        this.repository = repository;
        this.teamRepository = teamRepository;
    }

    public User createUser(User user) {
        return repository.save(user);
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
            throw new InvalidTeamException("User: " + user.getUsername() + " is already in a team");
        }

    }

    private TeamWeb convertToTeamWeb(Team team) {
        return new TeamWeb(team.getId(), team.getName(), team.getIsActive());
    }
}

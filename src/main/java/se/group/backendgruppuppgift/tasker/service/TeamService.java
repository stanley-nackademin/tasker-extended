package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTeamException;

import java.util.List;
import java.util.Optional;

@Service
public final class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    public Team updateTeam(Team team){
        return teamRepository.save(team);
    }

    public Optional<Team> deleteTeam(Long id){
        Optional<Team> team = teamRepository.findById(id);
        teamRepository.deleteById(id);

        return team;
    }

    public Optional<Team> getTeam(Long id){
        return teamRepository.findById(id);
    }

    public Iterable<Team> getAllTeams(){
        return teamRepository.findAll();
    }

    public void addUserToTeam(Team team, User user){
        userTeamValidation(user);
        maxUserLimitValidation(team);

        //Todo: how to add team without setters???

        Optional<User> optionalUser = userRepository.findByUserNumber(user.getUserNumber());
        //optionalUser.get().setTeam(team);

        userRepository.save(user);
    }

    private void maxUserLimitValidation(Team team) {
           List<User> users = userRepository.findByTeam(team);
                if(users.size()  >= 10){
                    throw new InvalidTeamException("Team: "+ team.getName() + " is full, max 10 users in a team");
                }
    }

    private void userTeamValidation(User user){
        if(user.getTeam().equals(null)){
            return;
        }else {
            throw new InvalidTeamException("User: " + user.getUsername() + " is already in a Team");
        }
    }

}

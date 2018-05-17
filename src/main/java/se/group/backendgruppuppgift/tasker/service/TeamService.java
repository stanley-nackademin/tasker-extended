package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public final class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public Team createTeam(Team team) {
        return teamRepository.save(new Team(team.getName(), true));
    }

    public Optional<Team> findTeam(Long id) {
        Optional<Team> result = teamRepository.findById(id);

        if (result.isPresent())
            return Optional.ofNullable(result.get());

        return Optional.empty();
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<User> getAllUserByTeamName(String teamName) {
        Optional<Team> result = teamRepository.findByName(teamName);
        List<User> users = new ArrayList<>();

        if (result.isPresent()) {
            Team team = result.get();
            users = userRepository.findUsersByTeamId(team.getId());
        }

        return users;
    }


    public Optional<Team> updateTeam(Long id, Team team) {
        Optional<Team> result = teamRepository.findById(id);

        if (result.isPresent()) {
            Team updatedTeam = result.get();

            if (!isBlank(team.getName()))
                updatedTeam.setName(team.getName());

            if (team.getIsActive() != null)
                updatedTeam.setIsActive(team.getIsActive());

            return Optional.ofNullable(teamRepository.save(updatedTeam));
        }

        return Optional.empty();
    }

    public Optional<Team> deleteTeam(Long id) {
        Optional<Team> result = teamRepository.findById(id);

        if (result.isPresent()) {
            teamRepository.deleteById(id);

            return Optional.ofNullable(result.get());
        }

        return Optional.empty();
    }
}

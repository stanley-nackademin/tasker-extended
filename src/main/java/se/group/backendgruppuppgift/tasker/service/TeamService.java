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
        return teamRepository.findById(id);
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public List<User> getAllUserByTeamId(Long id) {
        return teamRepository.findById(id)
                .map(u -> userRepository.findUsersByTeamId(u.getId()))
                .orElse(new ArrayList<>());
    }

    public Optional<Team> updateTeam(Long id, Team team) {
        Optional<Team> result = teamRepository.findById(id);

        if (result.isPresent()) {
            Team updatedTeam = result.get();

            if (!isBlank(team.getName())) {
                updatedTeam.setName(team.getName());
            }

            if (team.getIsActive() != null) {
                updatedTeam.setIsActive(team.getIsActive());
            }

            return Optional.of(teamRepository.save(updatedTeam));
        }

        return result;
    }

    public Optional<Team> deleteTeam(Long id) {
        Optional<Team> team = teamRepository.findById(id);

        if (team.isPresent()) {
            teamRepository.deleteById(id);
        }

        return team;
    }
}

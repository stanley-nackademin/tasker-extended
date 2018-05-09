package se.group.backendgruppuppgift.tasker.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.web.TeamWeb;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import java.util.Optional;

@Service
public final class TeamService {

    private final TeamRepository repository;

    public TeamService(TeamRepository teamRepository) {
        this.repository = teamRepository;
    }

    public Team createTeam(TeamWeb team) {
        return repository.save(new Team(team.getName(), team.getIsActive()));
    }

    public Optional<Team> updateTeam(Long id, TeamWeb team) {
        Optional<Team> result = repository.findById(id);

        if (result.isPresent()) {
            Team updatedTeam = result.get();

            if (!StringUtils.isBlank(team.getName())) {
                updatedTeam.setName(team.getName());
            }

            if(team.getIsActive() != null) {
                updatedTeam.setIsActive(team.getIsActive());
            }

            return Optional.ofNullable(repository.save(updatedTeam));
        }

        return Optional.empty();
    }

    public Optional<Team> deleteTeam(Long id){
        Optional<Team> team = repository.findById(id);
        repository.deleteById(id);

        return team;
    }

    public Optional<Team> getTeam(Long id){
        return repository.findById(id);
    }

    public Iterable<Team> getAllTeams(){
        return repository.findAll();
    }
}

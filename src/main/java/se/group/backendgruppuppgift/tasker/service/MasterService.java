package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;

@Service
public final class MasterService {

    private final TaskService taskService;
    private final TeamService teamService;

    public MasterService(TaskService taskService, TeamService teamService) {
        this.taskService = taskService;
        this.teamService = teamService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public TeamService getTeamService() {
        return teamService;
    }
}

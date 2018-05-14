package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;

@Service
public final class MasterService {

    private final TaskService taskService;
    private final TeamService teamService;
    private final UserService userService;

    public MasterService(TaskService taskService, TeamService teamService, UserService userService) {
        this.taskService = taskService;
        this.teamService = teamService;
        this.userService = userService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public TeamService getTeamService() {
        return teamService;
    }

    public UserService getUserService() {
        return userService;
    }
}

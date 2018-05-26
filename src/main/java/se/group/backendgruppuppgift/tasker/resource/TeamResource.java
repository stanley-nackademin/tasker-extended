package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.TeamWeb;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.resource.filter.AuthToken;
import se.group.backendgruppuppgift.tasker.service.TeamService;
import se.group.backendgruppuppgift.tasker.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("teams")
public final class TeamResource {

    @Context
    private UriInfo uriInfo;

    private final TeamService teamService;
    private final UserService userService;

    public TeamResource(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    @POST
    @AuthToken
    public Response createTeam(TeamWeb teamWeb) {
        Team result = teamService.createTeam(convertToTeam(teamWeb));
        TeamWeb webResult = convertToTeamWeb(result);

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(webResult.getId().toString())
                .toString()))
                .build();
    }

    @GET
    public List<TeamWeb> getAllTeams() {
        List<TeamWeb> result = new ArrayList<>();
        teamService.getAllTeams()
                .forEach(t -> result.add(convertToTeamWeb(t)));

        return result;
    }

    @GET
    @Path("{id}")
    public Response findTeam(@PathParam("id") Long id) {
        return teamService.findTeam(id)
                .map(t -> Response.ok(convertToTeamWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @GET
    @Path("{id}/users")
    public List<UserWeb> getAllUsersInTeam(@PathParam("id") Long id) {
        List<UserWeb> result = new ArrayList<>();
        teamService.getAllUserByTeamId(id)
                .forEach(t -> result.add(convertToUserWeb(t)));

        return result;
    }

    @PUT
    @Path("{id}")
    public Response updateTeam(@PathParam("id") Long id, TeamWeb teamWeb) {
        return teamService.updateTeam(id, convertToTeam(teamWeb))
                .map(t -> Response.ok(convertToTeamWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{id}/users")
    public Response assignTeamToUser(@PathParam("id") Long id, UserWeb userWeb) {
        return userService.assignTeam(id, convertToUser(userWeb))
                .map(t -> Response.ok(convertToTeamWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteTeam(@PathParam("id") Long id) {
        return teamService.deleteTeam(id)
                .map(todo -> Response.noContent())
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    private Team convertToTeam(TeamWeb teamWeb) {
        return new Team(teamWeb.getName(), teamWeb.getIsActive());
    }

    private TeamWeb convertToTeamWeb(Team team) {
        return new TeamWeb(team.getId(), team.getName(), team.getIsActive());
    }

    private User convertToUser(UserWeb userWeb) {
        return new User(userWeb.getUserNumber(), userWeb.getUsername(), userWeb.getFirstName(), userWeb.getLastName(), userWeb.getTeam());
    }

    private UserWeb convertToUserWeb(User user) {
        return new UserWeb(user.getUserNumber(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getIsActive(), user.getTeam());
    }
}

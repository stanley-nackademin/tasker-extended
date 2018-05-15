package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.web.TeamWeb;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.service.MasterService;
import se.group.backendgruppuppgift.tasker.service.TeamService;
import se.group.backendgruppuppgift.tasker.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("teams")
public final class TeamResource {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private MasterService service;

    @POST
    public Response createTeam(TeamWeb teamWeb) {
        TeamWeb result = service.getTeamService().createTeam(teamWeb);

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(result.getId().toString())
                .toString()))
                .build();
    }

    //Todo: add user to team method

    @GET
    public List<Team> getAllTeams() {
        return service.getTeamService().getAllTeams();
    }

    @GET
    @Path("{id}")
    public Response findTeam(@PathParam("id") Long id) {
        return service.getTeamService().findTeam(id)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateTeam(@PathParam("id") Long id, TeamWeb team) {
        return service.getTeamService().updateTeam(id, team)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{id}/adduser")
    public Response assignTeamToUser(@PathParam("id") Long id, UserWeb userWeb){
        UserService userService = service.getUserService();
        return userService.addTeam(id, userWeb)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteTeam(@PathParam("id") Long id) {
        return service.getTeamService().deleteTeam(id)
                .map(todo -> Response.status(NO_CONTENT))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @GET
    @Path("{teamName}/users")
    public Response getAllUsersInTeam(@PathParam("teamName") String name){

        return Response.ok(service.getTeamService().getAllUserByTeamName(name)).build();
    }
}

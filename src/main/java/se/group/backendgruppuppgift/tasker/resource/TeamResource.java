package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.service.TeamService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;

// TODO: 2018-05-10 Change team model to teamWeb

@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("teams")
public final class TeamResource {

    @Context
    private UriInfo uriInfo;

    private final TeamService service;

    public TeamResource(TeamService service) {
        this.service = service;
    }

    @POST
    public Response createTeam(Team team) {
        Team result = service.createTeam(team);

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(result.getId().toString())
                .toString()))
                .build();
    }

    //Todo: add user to team method

    @GET
    public List<Team> getAllTeams() {
        return service.getAllTeams();
    }

    @GET
    @Path("{id}")
    public Response findTeam(@PathParam("id") Long id) {
        return service.findTeam(id)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{id}")
    public Response updateTeam(@PathParam("id") Long id, Team team) {
        service.updateTeam(team);

        // TODO: 2018-05-10 Unfinished
        return service.findTeam(id)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteTeam(@PathParam("id") Long id) {
        return service.deleteTeam(id)
                .map(todo -> Response.status(NO_CONTENT))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }
}

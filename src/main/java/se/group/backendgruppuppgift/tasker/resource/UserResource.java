package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.Action;
import se.group.backendgruppuppgift.tasker.model.Issue;
import se.group.backendgruppuppgift.tasker.model.Task;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.IssueWeb;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.resource.converter.UserConverter;
import se.group.backendgruppuppgift.tasker.resource.filter.AuthToken;
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
@Path("users")
public final class UserResource {

    @Context
    private UriInfo uriInfo;

    private final UserService service;
    private final UserConverter converter;

    public UserResource(UserService service) {
        this.service = service;
        this.converter = new UserConverter();
    }

    @POST
    @AuthToken
    public Response createUser(UserWeb userWeb) {
        User result = service.createUser(converter.fromWebToEntityData(userWeb));
        userWeb = converter.fromEntityToWebData(result);

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(userWeb.getUserNumber().toString())
                .toString()))
                .build();
    }

    @GET
    @Path("{userNumber}")
    public Response getUser(@PathParam("userNumber") Long userNumber) {
        return service.findUserByUserNumber(userNumber)
                .map(u -> Response.ok(converter.fromEntityToWebData(u)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @GET
    public List<UserWeb> getUsers(@QueryParam("firstname") @DefaultValue("") String firstName,
                                  @QueryParam("lastname") @DefaultValue("") String lastName,
                                  @QueryParam("username") @DefaultValue("") String userName,
                                  @QueryParam("page") @DefaultValue("") String page) {

        List<UserWeb> result = new ArrayList<>();
        service.findAllUsersBy(firstName, lastName, userName, page)
                .forEach(u -> result.add(converter.fromEntityToWebData(u)));

        return result;
    }

    @PUT
    @Path("{usernumber}")
    public Response updateUser(@PathParam("usernumber") Long userNumber, UserWeb userWeb) {
        return service.updateUser(userNumber, converter.fromWebToEntityData(userWeb))
                .map(u -> Response.ok(converter.fromEntityToWebData(u)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{userNumber}/action")
    public void userDeactivator(@PathParam("userNumber") Long userNumber, Action action) {
        service.userActivator(userNumber, action);
    }

    @PUT
    @Path("{userNumber}/tasks")
    public Response addTaskToUser(@PathParam("userNumber") Long userNumber, TaskWeb taskweb) {
        return service.assignTaskToUser(userNumber, taskweb.getId())
                .map(t -> Response.ok(convertToTaskWeb(t)))
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @DELETE
    @Path("{userNumber}")
    public Response deleteUserByUserNumber(@PathParam("userNumber") Long userNumber) {
        return service.deleteUserByUserNumber(userNumber)
                .map(u -> Response.noContent())
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    private TaskWeb convertToTaskWeb(Task task) {
        return new TaskWeb(task.getId(), task.getDescription(), task.getStatus(), convertToIssueWeb(task.getIssue()));
    }

    private IssueWeb convertToIssueWeb(Issue issue) {
        return (issue != null) ? new IssueWeb(issue.getDescription()) : null;
    }
}

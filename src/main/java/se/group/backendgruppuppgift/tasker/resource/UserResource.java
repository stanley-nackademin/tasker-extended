package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.model.web.TaskWeb;
import se.group.backendgruppuppgift.tasker.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
@Component
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("users")
public final class UserResource {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private UserService service;

    public UserResource(UserService service) {
        this.service = service;
    }

    @POST
    public Response createUser(UserWeb user) {
        UserWeb result = service.createUser(user);

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(result.getUserNumber().toString())
                .toString()))
                .build();
    }

    @DELETE
    @Path("{userNumber}")
    public Response deleteUserByUserNumber(@PathParam ("userNumber") Long userNumber){
        Optional<UserWeb> result = service.deleteUserByUserNumber(userNumber);
        return result.map(r -> Response.status(NO_CONTENT)).orElse(Response.status(NOT_FOUND)).build();
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") Long id, User user){
        service.updateUser(user);
        return Response.status(NO_CONTENT).build();
    }

    @GET
    @Path("{userNumber}")
    public Response getUser(@PathParam("userNumber") Long userNumber) {
        return service.findUserByUserNumber(userNumber)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @GET
    @Path("/teams/{teamId}")
    public Response getAllUsersByTeam(@PathParam("teamId") Long teamId){
        return Response.ok(service.findUsersByTeamId(teamId)).build();
    }

    @GET
    public Response getUsers(@QueryParam("firstname") @DefaultValue("") String firstName,
                             @QueryParam("lastname" ) @DefaultValue("") String lastName,
                             @QueryParam("username") @DefaultValue("") String userName){

        List<User> users = service.findAllUsersBy(firstName.toLowerCase(),lastName.toLowerCase(),userName.toLowerCase());
        return Response.ok(service.findAllUsersBy(firstName.toLowerCase(),lastName.toLowerCase(),userName.toLowerCase())).build();
    }

    //TODO ------------------------DENNA ÄR INTE KLAR.
    @PUT
    @Path("{usernumber}/tasks/{taskid}")
    public Response updateUserTask(@PathParam("usernumber") Long userNumber,@PathParam("taskid") Long taskId){
        return service.updateUserTask(userNumber,taskId)
                .map(Response::ok)
                .orElse(Response.status(NOT_FOUND))
                .build();
    }

    @PUT
    @Path("{userNumber}/activate")
    public Response userDeActivator(@PathParam("userNumber")Long userNumber){
//        return service.userActivator(userNumber)
//                .map(Response::ok)
//                .orElse(Response.status(NOT_FOUND))
//                .build();
        service.userActivator(userNumber);
        return Response.status(NO_CONTENT).build();
    }
}

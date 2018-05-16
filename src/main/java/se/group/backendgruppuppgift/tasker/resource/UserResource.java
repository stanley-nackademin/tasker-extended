package se.group.backendgruppuppgift.tasker.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.UserWeb;
import se.group.backendgruppuppgift.tasker.resource.converter.UserConverter;
import se.group.backendgruppuppgift.tasker.service.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.*;

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
    public Response createUser(UserWeb userWeb) {
        Optional<User> user = converter.getOptionalUser(userWeb);
        service.createUser(user.get());
        userWeb = converter.getOptionalUserWeb(user.get()).get();

        return Response.created(URI.create(uriInfo
                .getAbsolutePathBuilder()
                .path(userWeb.getUserNumber().toString())
                .toString()))
                .build();
    }

    @PUT
    @Path("{usernumber}")
    public Response updateUser(@PathParam("usernumber") Long userNumber, UserWeb userWeb){
        Optional<User> newUSer = UserConverter.getOptionalUser(userWeb);

//        return service.updateUser(userNumber,UserConverter.getOptionalUser(userWeb))
//                .map(t -> Response.ok(UserConverter.getOptionalUserWeb(t)))
//                .orElse(Response.status(NOT_FOUND))
//                .build();
    }

    @DELETE
    @Path("{userNumber}")
    public Response deleteUserByUserNumber(@PathParam ("userNumber") Long userNumber){
        Optional<User> task = service.deleteUserByUserNumber(userNumber);
        Optional<UserWeb> result = converter.getOptionalUserWeb(task.get());
        return result.map(r -> Response.status(NO_CONTENT)).orElse(Response.status(NOT_FOUND)).build();
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
        service.userActivator(userNumber);
        return Response.status(NO_CONTENT).build();
    }
}
